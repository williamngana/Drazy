package its.lit.drazeyyy;


import com.facebook.FacebookSdk;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private User user;

    SupportMapFragment sMapFragment;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ImageButton droneImageButton;
    private Intent getIntent;
    private Intent pickIntent;
    private Intent chooserIntent;

    private LocationManager mLocationManager;
    private Location currBestLocation;

    static final int PICK_IMAGE = 1;
    static final int CAMERA_IMAGE_FOR_TAG = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: get stuff from DB and fill in tagged/etc.
        user = new User(null, null);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        getIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);;
        getIntent.setType("image/");

        pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        chooserIntent = Intent.createChooser(getIntent, "Choose your drone");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        sMapFragment = SupportMapFragment.newInstance();

        sharedPreferences = getSharedPreferences(User.USER_PREF_DB, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: dialog that pops up, can choose to take a picture,
                // write a message, or tag an alert (i.e. police or geese, etc.)
                // at your current location
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_IMAGE_FOR_TAG);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (droneImageButton == null) {
                    String currPath = sharedPreferences.getString(User.DRONE_IMG, "");

                    droneImageButton = (ImageButton) findViewById(R.id.drone_image_button);
                    if (!currPath.equalsIgnoreCase("")){
                        Log.d("new path is...",currPath);
                        File imageFile = new File(currPath);
                        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        droneImageButton.setImageBitmap(bitmap);
                    }

                    droneImageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivityForResult(getIntent, PICK_IMAGE);
                        }
                    });
                }
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        sMapFragment.getMapAsync(this);

        sFm.beginTransaction().replace(R.id.map, sMapFragment).commit();

        currBestLocation = getLastBestLocation();



    }

    private Location getLastBestLocation() throws SecurityException {
        Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return locationGPS;
        }
        else {
            return locationNet;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        int id = item.getItemId();

        if (sMapFragment.isAdded())
            sFm.beginTransaction().hide(sMapFragment).commit();

//        if (id == R.id.nav_camara) {
//
//            if (!sMapFragment.isAdded())
//                sFm.beginTransaction().add(R.id.map, sMapFragment).commit();
//            else
//                sFm.beginTransaction().show(sMapFragment).commit();
//
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {

            Uri uri = data.getData();

            String realPath = getRealPathFromURI(this, uri);

        }
        else if (requestCode == CAMERA_IMAGE_FOR_TAG && resultCode == RESULT_OK &&
                data != null && data.getData() != null){

            // TODO: leads to new activity where u can put in a message with the photo
            // TODO: send this to database

            Uri uri = data.getData();
            String realPath = getRealPathFromURI(this, uri);
            currBestLocation = getLastBestLocation();
            user.addTag(currBestLocation.getLongitude(),
                    currBestLocation.getLatitude(), "", realPath);


        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        currBestLocation = getLastBestLocation();

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(currBestLocation.getLatitude(),
                        currBestLocation.getLongitude()))
                .title("Me!").icon(BitmapDescriptorFactory.fromResource(R.drawable.droneicon)));
        LatLng latLng = new LatLng(currBestLocation.getLatitude(), currBestLocation.getLongitude());

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(currBestLocation.getLatitude(), currBestLocation.getLongitude()))
                .radius(500);
        circleOptions.fillColor(R.color.grey);
        Circle circle = googleMap.addCircle(circleOptions);


        double latAirport = 43.4558;
        double lngAirport = -80.3858;

        LatLng latLngAirport = new LatLng(latAirport, lngAirport);
        googleMap.addMarker(new MarkerOptions()
                .position(latLngAirport)
                .title("Airport info here!"));
        CircleOptions circleOptionsAirport = new CircleOptions()
                .center(latLngAirport)
                .radius(5000);
        circleOptions.fillColor(R.color.red);
        Circle circleAirport = googleMap.addCircle(circleOptionsAirport);


        final double latLaurier = 43.4724;
        final double lngLaurier = -80.5263;

        LatLng latLngLaurier = new LatLng(latLaurier, lngLaurier);
        Marker laurierMarker = googleMap.addMarker(new MarkerOptions()
                .position(latLngLaurier)
                .title("Laurier info here!").icon(BitmapDescriptorFactory.fromResource(R.drawable.droneicon2)));
        CircleOptions circleOptionsLaurier = new CircleOptions()
                .center(latLngLaurier)
                .radius(500);
        circleOptions.fillColor(R.color.orange);
        Circle circleLaurier = googleMap.addCircle(circleOptionsLaurier);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng1 = marker.getPosition();
                if (latLng1.latitude == latLaurier && latLng1.longitude == lngLaurier){

                }
                return true;
            }
        });
    }

}
