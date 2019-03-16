package ro.cosma.andrei.sunnydays.pages.location;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.activity.MainActivity;
import ro.cosma.andrei.sunnydays.activity.listener.IMainActivityListener;
import ro.cosma.andrei.sunnydays.async.location.DeleteLocationTask;
import ro.cosma.andrei.sunnydays.async.location.GetLocationIdListTask;
import ro.cosma.andrei.sunnydays.async.location.GetLocationTask;
import ro.cosma.andrei.sunnydays.async.location.ParseLocationTask;
import ro.cosma.andrei.sunnydays.async.location.SaveLocationTask;
import ro.cosma.andrei.sunnydays.async.location.SaveLocationsTask;
import ro.cosma.andrei.sunnydays.async.location.listener.IDeleteLocationListener;
import ro.cosma.andrei.sunnydays.async.location.listener.IGetLocationIdListListener;
import ro.cosma.andrei.sunnydays.async.location.listener.IGetLocationListener;
import ro.cosma.andrei.sunnydays.async.location.listener.IParseLocationListener;
import ro.cosma.andrei.sunnydays.async.location.listener.ISaveLocationListener;
import ro.cosma.andrei.sunnydays.async.location.listener.ISaveLocationsListener;
import ro.cosma.andrei.sunnydays.async.weather.DeleteWeatherForecastByCityTask;
import ro.cosma.andrei.sunnydays.bean.LocationBean;
import ro.cosma.andrei.sunnydays.components.DeleteDialog;
import ro.cosma.andrei.sunnydays.components.listener.IDeleteDialogListener;
import ro.cosma.andrei.sunnydays.pages.location.adapter.LocationRecyclerViewAdapter;
import ro.cosma.andrei.sunnydays.pages.location.dialog.LocationInputDialog;
import ro.cosma.andrei.sunnydays.pages.location.dialog.listener.ILocationInputDialogListener;
import ro.cosma.andrei.sunnydays.pages.location.listeners.ILocationRecyclerViewAdapterListener;
import ro.cosma.andrei.sunnydays.parser.JSonWeatherParser;
import ro.cosma.andrei.sunnydays.service.WeatherService;
import ro.cosma.andrei.sunnydays.utils.AppConstants;
import ro.cosma.andrei.sunnydays.utils.AppUtils;

import static ro.cosma.andrei.sunnydays.utils.AppConstants.MODE_METRIC;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_LOCATION;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_TEMPERATURE;


/*Created by Cosma Andrei
 * 9/26/2017*/
public class LocationFragment extends Fragment implements View.OnClickListener, ISaveLocationsListener, IDeleteDialogListener, ISaveLocationListener, ILocationInputDialogListener, IGetLocationListener, SwipeRefreshLayout.OnRefreshListener, ILocationRecyclerViewAdapterListener, IDeleteLocationListener, IGetLocationIdListListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    public static final String TAG = LocationFragment.class.getSimpleName();

    private IMainActivityListener mActivityListener;
    private ProgressDialog pd;

    private List<LocationBean> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private LocationRecyclerViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View progressView;
    private SearchView actionBarSearchView;
    private FloatingActionButton fab;

    private SharedPreferences preferences;
    private String tempUnit = "";
    private String currentLocationName = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        tempUnit = preferences.getString(PREFERENCES_TEMPERATURE, MODE_METRIC);
        currentLocationName = preferences.getString(PREFERENCES_LOCATION, null);

        setHasOptionsMenu(true);

        if (adapter == null) {
            adapter = new LocationRecyclerViewAdapter(this, tempUnit, list, currentLocationName);
        }

        /*Permite unui view din recycle view sa fie miscat de la stanga la dreapta.*/
//        ItemTouchHelper helper = new ItemTouchHelper(
//                new ItemTouchHelper.SimpleCallback(0,
//                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//                    @Override
//                    public boolean onMove(RecyclerView recyclerView,
//                                          RecyclerView.ViewHolder viewHolder,
//                                          RecyclerView.ViewHolder target) {
//                        return false;
//                    }
//
//                    @Override
//                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
//                                         int direction) {
//                    }
//                });
//        helper.attachToRecyclerView(recyclerView);



        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView;
        swipeRefreshLayout.setOnRefreshListener(this);

        progressView = rootView.findViewById(R.id.progress);
        fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof IMainActivityListener) {
            mActivityListener = (IMainActivityListener) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        ((AppCompatActivity) getActivity()).findViewById(R.id.app_bar_layout).setElevation(0);
        mActivityListener.setCurrentFragmentTag(TAG);
        getActivity().setTitle(getString(R.string.location));
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mActivityListener = null;
        super.onDestroy();
    }

    @Override
    public void onNavigateToCurrentWeather(LocationBean bean) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString(AppConstants.PREFERENCES_LOCATION, bean.getCity()).apply();
        mActivityListener.onNavigateToCurrentWeather();
    }

    @Override
    public void onDeletePressed(int id, int adapterPosition) {
        DeleteDialog.newInstance(this, getString(R.string.delete_location), id, adapterPosition).show(getFragmentManager(), DeleteDialog.TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.location, menu);
        actionBarSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        actionBarSearchView.setOnQueryTextListener(this);
        actionBarSearchView.setOnCloseListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onRefresh() {
        if (AppUtils.hasConnectionToInternet(getContext())) {
            fetchLocations(getIdList());
        } else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void showGetLocationProgressDialog() {
        if (isAdded()) {
            pd = ProgressDialog.show(getActivity(), "", getString(R.string.getting_location));
        }
    }

    @Override
    public void onStart() {
        if (AppUtils.hasConnectionToInternet(getContext())) {
            new GetLocationIdListTask(this, getContext()).execute();
        } else {
            new GetLocationTask(this, getContext()).execute();
        }
        super.onStart();
    }

    @Override
    public void onSuccessGetLocation(List<LocationBean> bean) {
        list.clear();
        list.addAll(bean);
        adapter.notifyDataSetChanged();
        setViewsVisible();
    }

    @Override
    public void dismissGetLocationProgressDialog() {
        if (pd != null) {
            pd.dismiss();
        }
    }

    @Override
    public void onErrorGetLocation() {
        Toast.makeText(getActivity(), getString(R.string.error_getting_location), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPositiveAction(String locationName) {
        if (AppUtils.hasConnectionToInternet(getContext())) {
            try {
                WeatherService.getInstance().getCurrentWeather(getContext(), locationName, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            new SaveLocationTask(LocationFragment.this, getContext()).execute(JSonWeatherParser.getInstance().getLocation(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void showSaveLocationProgressDialog() {
        if (isAdded()) {
            pd = ProgressDialog.show(getActivity(), "", getString(R.string.saving_location));
        }
    }

    @Override
    public void onSuccessSaveLocation(LocationBean bean) {
        if (!AppUtils.LocationArrayContains(bean.getId(), list)) {
            list.add(bean);
            adapter.notifyItemChanged(list.size() - 1);
        }
    }

    @Override
    public void dismissSaveLocationProgressDialog() {
        if (pd != null) {
            pd.dismiss();
        }
        Toast.makeText(getActivity(), getString(R.string.location_saved), Toast.LENGTH_SHORT).show();
        recyclerView.scrollToPosition(adapter.getItemCount());
    }

    @Override
    public void onErrorSaveLocation() {
        Toast.makeText(getActivity(), getString(R.string.error_saving_location), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSaveLocationsProgressDialog() {

    }

    @Override
    public void onSuccessSaveLocations(List<LocationBean> beanList) {
        list.clear();
        list.addAll(beanList);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        setViewsVisible();
    }

    @Override
    public void dismissSaveLocationsProgressDialog() {

    }

    @Override
    public void onErrorSaveLocations() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPositiveDelete(int id, int positionInAdapter) {
        new DeleteLocationTask(this, getContext()).execute(id, positionInAdapter);
    }

    @Override
    public void showDeleteLocationProgressDialog() {
        if (isAdded()) {
            pd = ProgressDialog.show(getActivity(), "", getString(R.string.deleting_location));
        }
    }

    @Override
    public void onSuccessDeleteLocation(int positionInAdapter) {
        //TODO:de sters si din baza de date toate intrarile
        new DeleteWeatherForecastByCityTask(getContext()).execute(list.get(positionInAdapter).getCity());

        list.remove(positionInAdapter);
        adapter.notifyItemRemoved(positionInAdapter);
        Toast.makeText(getActivity(), getString(R.string.success_deleting_location), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void dismissDeleteLocationProgressDialog() {
        if (pd != null) {
            pd.dismiss();
        }
    }

    @Override
    public void onErrorDeleteLocation() {
        Toast.makeText(getActivity(), getString(R.string.error_deleting_location), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGetLocationIdListProgressDialog() {

    }

    @Override
    public void onSuccessGetLocationIdList(List<Integer> idList) {
        fetchLocations(AppUtils.toStringList(idList));
    }

    @Override
    public void dismissGetLocationIdListProgressDialog() {

    }

    @Override
    public void onErrorGetLocationIdList() {

    }

    public List<String> getIdList() {
        List<String> list = new ArrayList<>();
        for (LocationBean bean : this.list) {
            list.add(String.valueOf(bean.getId()));
        }
        return list;
    }


    private void fetchLocations(List<String> idList) {
        try {
            WeatherService.getInstance().getCurrentWeather(getContext(),
                    idList,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            new ParseLocationTask(new IParseLocationListener() {
                                @Override
                                public void showParseLocationProgressDialog() {

                                }

                                @Override
                                public void onSuccessParseLocation(List<LocationBean> bean) {
                                    new SaveLocationsTask(LocationFragment.this, LocationFragment.this.getContext()).execute(bean);
                                }

                                @Override
                                public void dismissParseLocationProgressDialog() {

                                }

                                @Override
                                public void onErrorParseLocation() {
                                    new GetLocationTask(LocationFragment.this, LocationFragment.this.getContext()).execute();
                                }
                            }).execute(response);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            swipeRefreshLayout.setRefreshing(false);
                            if (list == null || list.size() == 0) {
                                setViewsVisible();
                            }
                            Toast.makeText(getActivity(), getString(R.string.error_getting_location), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void setViewsVisible() {
        progressView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<LocationBean> searchedData = new ArrayList<>();

        for (LocationBean locationBean : list) {
            if (locationBean.getCity().equalsIgnoreCase(newText) ||
                    locationBean.getCity().toLowerCase().contains(newText.toLowerCase()) ||
                    locationBean.getCountry().equalsIgnoreCase(newText) ||
                    locationBean.getCountry().toLowerCase().contains(newText.toLowerCase())) {
                searchedData.add(locationBean);
            }
        }
        adapter.setNewDataset(searchedData);
        return true;
    }

    @Override
    public boolean onClose() {
        adapter.setNewDataset(list);
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fab) {
            LocationInputDialog.newInstance(this).show(getFragmentManager(), "");
        }
    }
}
