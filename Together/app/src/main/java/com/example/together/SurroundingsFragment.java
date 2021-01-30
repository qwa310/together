package com.example.together;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    public double longitude, latitude, startLatitude, startLongitude, endLatitude, endLongitude;
    double distanceLength;
    TMapPoint tMapPointCurrent, tMapPointTo, tMapPointFrom;
    boolean count = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surroundings, container, false);

        /**T Map View**/
        tMapView = new TMapView(this.getContext());

        /**T Map API Authorization**/
        tMapView.setSKTMapApiKey(API_Key);

        /**T Map SETTINGS**/
        tMapView.setZoomLevel(15);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        //tMapView.setIconVisibility(true);

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
        tMapView.setTrackingMode(true);
        //tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER);
        //tMapView.setCompassMode(true);


        /**GPS Settings**/
        final LocationManager lm = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener);


        /**Set Floating Button**/
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fabNavigation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMapPointFrom = new TMapPoint(startLatitude, startLongitude);
                endLatitude = 35.18091375602048;
                endLongitude = 129.07494408467085;
                tMapPointTo = new TMapPoint(endLatitude, endLongitude);
                distanceLength = distance(startLatitude, startLongitude, endLatitude, endLongitude, "kilometer");
                String msg = "목적지까지 약 " + (int)distanceLength + "km 거리 입니다.";
                toastDisplay(msg);
                DrawPath();
            }
        });

        return view;
    }

    /**Toast Settings**/
    public void toastDisplay(String msg){
        Toast tos = null;
        if(tos != null) {
            tos.cancel();
        }
        tos = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        tos.show();
    }

    /**
     * 두 지점간의 거리 계산
     *
     * @param lat1 지점 1 위도
     * @param lon1 지점 1 경도
     * @param lat2 지점 2 위도
     * @param lon2 지점 2 경도
     * @param unit 거리 표출단위
     * @return
     */
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if(unit == "meter"){
            dist = dist * 1609.344;
        }
        return (dist);
    }


    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    /**Updating Current Location**/
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            /**Real time location**/
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                tMapView.setLocationPoint(longitude, latitude);
                tMapView.setCenterPoint(longitude, latitude, true);
                //TMapPoint arrTMapPoint = tMapView.getCenterPoint();
                //Log.d("TmapTest", "" + longitude + "," + latitude);
                tMapPointCurrent = new TMapPoint(latitude, longitude);
                if(distance(latitude, longitude, endLatitude, endLongitude, "kilometer") < 0.5){
                    String msg = "목적지에 도착하였습니다.";
                    toastDisplay(msg);
                }
                currentMarker(tMapPointCurrent);
                if (count) {
                    startLatitude = latitude;
                    startLongitude = longitude;
                    count = false;
                }
                searchPOI(tMapPointCurrent);
            }
        }
    };


    /**Draw Path**/
    public void DrawPath(){
        setMarker();
        TMapPolyLine polyLine = new TMapPolyLine();
        PathAsync pathAsync = new PathAsync();
        pathAsync.execute(polyLine);
    }

    /**Marker for Current Location**/
    public void currentMarker(TMapPoint tMapPointCurrent){
        TMapMarkerItem tMapMarkerItemCurrent = new TMapMarkerItem();
        Bitmap currentPoint = createMarkerIcon(50, 50, R.drawable.colorfulcircled);
        tMapMarkerItemCurrent.setIcon(currentPoint);
        tMapMarkerItemCurrent.setTMapPoint(tMapPointCurrent);
        tMapView.addMarkerItem("tMapMarkerItemCurrent", tMapMarkerItemCurrent);
    }

    /**Marker for Starting Point/Destination**/
    public void setMarker(){
        TMapMarkerItem tMapMarkerItemFrom = new TMapMarkerItem();
        TMapMarkerItem tMapMarkerItemTo = new TMapMarkerItem();
        Bitmap startPoint = createMarkerIcon(80, 80, R.drawable.red_marker);
        tMapMarkerItemFrom.setIcon(startPoint);
        tMapMarkerItemFrom.setTMapPoint(tMapPointFrom);
        tMapView.addMarkerItem("tMapMarkerItemFrom", tMapMarkerItemFrom);
        Bitmap endPoint = createMarkerIcon(80, 80, R.drawable.blue_marker);
        tMapMarkerItemTo.setIcon(endPoint);
        tMapMarkerItemTo.setTMapPoint(tMapPointTo);
        tMapView.addMarkerItem("tMapMarkerItemTo", tMapMarkerItemTo);
    }

    /**Create Marker Icon**/
    public Bitmap createMarkerIcon(int width, int height, int image) {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), image);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height,false);
        return bitmap;
    }

    /**Finder & Marker for Police Station**/
    public void searchPOI(TMapPoint tMapPoint) {
        TMapData tMapData = new TMapData();
        final ArrayList<TMapPoint> searchPoint = new ArrayList<>();
        /*final ArrayList<String> arrTitle = new ArrayList<>();
        final ArrayList<String> arrAddress = new ArrayList<>();*/

        /**Searching for Positions**/
        tMapData.findAroundNamePOI(tMapPoint, "경찰서;소방서;파출소;지구대;치안센터", 30,5000,
                new TMapData.FindAroundNamePOIListenerCallback() {
                    @Override
                    public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                        for (int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem item = poiItem.get(i);
                            searchPoint.add(item.getPOIPoint());
                            /*arrTitle.add(item.getPOIName());
                            arrAddress.add(item.upperAddrName + " " +
                                    item.middleAddrName + " " + item.lowerAddrName);*/
                        }
                        setMultiMarkers(searchPoint);
                        //setMultiMarkers(arrTMapPoint, arrTitle, arrAddress);
                    }
                });
    }

    /**Set Multiple BalloonMarks**/
    public void setMultiMarkers(ArrayList<TMapPoint> searchSpot) { // ArrayList<String> arrTitle, ArrayList<String> arrAddress
        for( int i = 0; i < searchSpot.size(); i++ ) {
            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
            Bitmap bitmap = createMarkerIcon(40, 40, R.drawable.green_marker);
            tMapMarkerItem.setIcon(bitmap);
            tMapMarkerItem.setTMapPoint(searchSpot.get(i));
            tMapView.addMarkerItem("markerItem" + i, tMapMarkerItem);
            //setBalloonView(tMapMarkerItem, arrTitle.get(i), arrAddress.get(i));
        }
    }

    /**Set BalloonMarks**/
    /*public void setBalloonView(TMapMarkerItem marker, String title, String address) {
     marker.setCanShowCallout(true);
     }
     */



    /**Asynchronous Processing**/
    class PathAsync extends AsyncTask<TMapPolyLine, Void, TMapPolyLine> {
        @Override
        protected TMapPolyLine doInBackground(TMapPolyLine... tMapPolyLines) {
            TMapPolyLine tMapPolyLine = tMapPolyLines[0];
            try {
                tMapPolyLine = new TMapData().findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointFrom, tMapPointTo);
                tMapPolyLine.setLineColor(Color.MAGENTA);
                tMapPolyLine.setLineWidth(3);

            }
            catch(Exception e) {
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
                    final LocationManager lm = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this.getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener);
                } else {
                    Log.d("locationTest", "동의 거부");
                }
                return;
            }
        }
    }

};
