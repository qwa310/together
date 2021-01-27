package com.example.together;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import java.util.ArrayList;

public class SurroundingsFragment extends Fragment {
    private static String API_Key = "l7xx52f2020a27a646b995dab1ba21acdfd7";
    TMapView tMapView;
    TMapGpsManager tMapGPS;
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    double longitude, latitude;
    TMapPoint tMapPointStart, tMapPointEnd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surroundings, container, false);

        /**T Map View**/
        tMapView = new TMapView(this.getContext());

        /**T Map API Authorization**/
        tMapView.setSKTMapApiKey(API_Key);

        /**T Map SETTINGS**/
        tMapView.setZoomLevel(15);
        tMapView.setIconVisibility(true);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        /**T Map View Using Linear Layout**/
        RelativeLayout relativeLayoutTmap = view.findViewById(R.id.map);
        relativeLayoutTmap.addView(tMapView);

        /**Request For GPS permission**/
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
        }

        /**GPS using T Map**/
        tMapGPS = new TMapGpsManager(this.getContext());

        /**Initial GPS Setting**/
        tMapGPS.setMinTime(1000);
        tMapGPS.setMinDistance(10);
        tMapGPS.setProvider(tMapGPS.GPS_PROVIDER);
        //tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER);
        tMapView.setTrackingMode(true);
        //tMapView.setCompassMode(true);

        setGps();
        return view;
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            /**Real time location**/
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                tMapView.setLocationPoint(longitude, latitude);
                tMapView.setCenterPoint(longitude, latitude,true);
                TMapPoint arrTMapPoint = tMapView.getCenterPoint();
                Log.d("TmapTest", "" + longitude + "," + latitude);
                DrawPath();
                searchPOI();
            }
        }

        public void DrawPath(){
            tMapPointStart = new TMapPoint(latitude, longitude);
            tMapPointEnd = new TMapPoint(35.18091375602048, 129.07494408467085);
            TMapPolyLine polyLine = new TMapPolyLine();
            PathAsync pathAsync = new PathAsync();
            pathAsync.execute(polyLine);
        }

        public void searchPOI() {
            TMapData tMapData = new TMapData();
            TMapPoint tMapPoint = new TMapPoint(latitude, longitude);
            final ArrayList<TMapPoint> arrTMapPoint = new ArrayList<>();
            final ArrayList<String> arrTitle = new ArrayList<>();
            final ArrayList<String> arrAddress = new ArrayList<>();

            /**Find Positions**/
            tMapData.findAroundNamePOI(tMapPoint, "경찰서;소방서;파출소;지구대;치안센터", 8, 200,
                    new TMapData.FindAroundNamePOIListenerCallback() {
                        @Override
                        public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                            for (int i = 0; i < poiItem.size(); i++) {
                                TMapPOIItem item = poiItem.get(i);
                                arrTMapPoint.add(item.getPOIPoint());
                                arrTitle.add(item.getPOIName());
                                arrAddress.add(item.upperAddrName + " " +
                                        item.middleAddrName + " " + item.lowerAddrName);
                            }
                            setMultiMarkers(arrTMapPoint, arrTitle, arrAddress);
                        }
                    });
        }

        /**Set Multiple BalloonMarks**/
        public void setMultiMarkers(ArrayList<TMapPoint> arrTPoint, ArrayList<String> arrTitle, ArrayList<String> arrAddress) {
            for( int i = 0; i < arrTPoint.size(); i++ ) {
                TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
                tMapMarkerItem.setTMapPoint(arrTPoint.get(i));
                tMapView.addMarkerItem("markerItem" + i, tMapMarkerItem);
                setBalloonView(tMapMarkerItem, arrTitle.get(i), arrAddress.get(i));
            }
        }

        /**Set BalloonMarks**/
        public void setBalloonView(TMapMarkerItem marker, String title, String address) {
            marker.setCanShowCallout(true);
        }
    };

    /**Asynchronous Processing**/
    class PathAsync extends AsyncTask<TMapPolyLine, Void, TMapPolyLine> {
        @Override
        protected TMapPolyLine doInBackground(TMapPolyLine... tMapPolyLines) {
            TMapPolyLine tMapPolyLine = tMapPolyLines[0];
            try {
                tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd);
                tMapPolyLine.setLineColor(Color.BLUE);
                tMapPolyLine.setLineWidth(3);


            }catch(Exception e) {
                e.printStackTrace();
                Log.e("error",e.getMessage());
            }
            return tMapPolyLine;
        }

        @Override
        protected void onPostExecute(TMapPolyLine tMapPolyLine) {
            super.onPostExecute(tMapPolyLine);
            tMapView.addTMapPolyLine("Line1", tMapPolyLine);
        }
    }

    /**GPS Permission**/
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setGps();
                } else {
                    Log.d("locationTest", "동의 거부");
                }
                return;
            }
        }
    }

    /**GPS Settings**/
    public void setGps() {
        final LocationManager lm = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener);
    }
};
