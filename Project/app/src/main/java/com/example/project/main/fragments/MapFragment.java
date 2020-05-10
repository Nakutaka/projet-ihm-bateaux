package com.example.project.main.fragments;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.R;
import com.example.project.main.ReportItemizedOverlay;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MapFragment extends Fragment implements IGPSActivity {

    private GPSFragment gpsFragment;

    //List<OverlayItem> items;// = new ArrayList<>();
    private MyLocationNewOverlay myPosition;
    private MapView map;
    private final OnlineTileSourceBase seaMarks = TileSourceFactory.OPEN_SEAMAP;
    //private FrameLayout reportDetails;
    ReportItemizedOverlay mOverlay;
    IMapController mapController;
    boolean onScroll = false;

    /**********User Settings (default value for the time being)*********/
    private double zoom = 15.5;


    public MapFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Configuration.getInstance().load(getContext(),
                PreferenceManager.getDefaultSharedPreferences(getContext()));*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map,container,false);

        map = rootView.findViewById(R.id.map_not_working_view);

        gpsFragment = new GPSFragment( this );

        FragmentTransaction gpsTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        gpsTransaction.replace( R.id.gpsLocation, gpsFragment );
        gpsTransaction.commit();

        setupMap();
        recenter();
        return rootView;
    }

    /*@Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_map, container, false);
        //map = rootView.findViewById(R.id.map_view);
        //reportDetails = rootView.findViewById(R.id.frame_layout_report_details);

        /*rootView.findViewById(R.id.fab_rain).setOnClickListener(this);*/

      /*  return rootView;
    }*/

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    /*******************************************************************/

    /////////////////////////////////////////////////////////////////////

    ///use it could be good if user taps somewhere else on the map
    /*void switchCoordinatesVisibility() {
        if (currentLocation.isShown())
            currentLocation.setVisibility(View.INVISIBLE);
        else
            currentLocation.setVisibility(View.VISIBLE);
    }*/


    public double getLastLatitude() {
        Location lastLocation = gpsFragment.getCurrentLocation();

        if(lastLocation == null) return -1;
        return lastLocation.getLatitude();
        //return Objects.requireNonNull(currentLocation).getLatitude();
    }

    public double getLastLongitude() {
        Location lastLocation = gpsFragment.getCurrentLocation();

        if(lastLocation == null) return -1;
        return lastLocation.getLongitude();
        //return Objects.requireNonNull(currentLocation).getLongitude();
    }
    /********************************************************************/

    public void recenterButtonAction() {
        focus(true);
        onScroll = false;
    }

    public void focus(boolean resetZoom) {
        recenter();
        if (resetZoom) mapController.setZoom(zoom);
    }

    private void recenter() {
        Location lastLocation = gpsFragment.getCurrentLocation();

        if (lastLocation != null) {
            mapController.animateTo(new GeoPoint(lastLocation.getLatitude(),
                    lastLocation.getLongitude()));
            // Resetting zoom should be donne when pressing recenter button only
            //mapController.setZoom(zoom);
        }
        else {
            Toast.makeText(
                    this.getContext(),
                    "Location is still loading..",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void updateMap(ReportItemizedOverlay newOverlayItems) {
        map.getOverlays().remove(mOverlay);
        mOverlay = newOverlayItems;
        //mOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(mOverlay);
    }

    //////////////////////////////////////////////////////////////////////

    /*************************Map setup***************************/
    private void setupMap() {
        //map = mainActivity.findViewById(R.id.map_view);
        if(map!=null) mapController = map.getController();
        else {
            Toast.makeText(
                    getContext(),"pb!!!!!!!",
                    Toast.LENGTH_SHORT).show();
        }
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setUseDataConnection(true);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBackgroundColor(Color.TRANSPARENT);
        final MapTileProviderBase tileProvider = new MapTileProviderBasic(
                getContext(), seaMarks);
        final TilesOverlay seaMarksOverlay = new TilesOverlay(tileProvider,
                getContext());
        seaMarksOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
        map.getOverlays().add(seaMarksOverlay);
        myPosition = new MyLocationNewOverlay(map);
        myPosition.enableFollowLocation();
        map.getOverlays().add(myPosition); //User's position on the map
        map.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                onScroll = true;
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                onScroll = true;
                return false;
            }
        });
    }
/////////////////////////////////////////////////////////////////////////////

}
