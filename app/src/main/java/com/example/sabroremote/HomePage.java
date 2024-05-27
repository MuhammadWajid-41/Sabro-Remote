package com.example.sabroremote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import android.Manifest;


import androidx.fragment.app.Fragment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

//dexter
import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import java.util.List;
//

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePage extends Fragment {

    Button btnToggle;
    TextView txtRES;

    private String led_on_off="OFF";

    private OkHttpClient client = new OkHttpClient();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePage.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePage newInstance(String param1, String param2) {
        HomePage fragment = new HomePage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);


        //------------------

        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        NetworkRequest request = builder.build();
        connManager.requestNetwork(request, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                connManager.bindProcessToNetwork(network);
            }
        });

        btnToggle = view.findViewById(R.id.button_toggle_led);

        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(led_on_off.equals("OFF"))
                {
                    sendCommand("ON");
                }
                else
                {
                    sendCommand("OFF");
                }

            }
        });

        //------------------


        //===============ASK PERMSISSIONS ====================//

        // Button click or other trigger to request permissions//
        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.CHANGE_NETWORK_STATE,
                        Manifest.permission.WRITE_SETTINGS
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // Handle permission results
                        if (report.areAllPermissionsGranted()) {
                            // All permissions granted, proceed with functionality
                            // (e.g., access network resources, check Wi-Fi status)
                        }

                        // Check for individually granted/denied permissions
                        for (PermissionGrantedResponse response : report.getGrantedPermissionResponses()) {
                            if (response.getPermissionName().equals(Manifest.permission.INTERNET)) {
                                // Internet permission granted
                            } else if (response.getPermissionName().equals(Manifest.permission.ACCESS_WIFI_STATE)) {
                                // Access WiFi State permission granted
                            } else if (response.getPermissionName().equals(Manifest.permission.ACCESS_NETWORK_STATE)) {
                                // Access Network State permission granted
                            }
                        }

                        for (PermissionDeniedResponse response : report.getDeniedPermissionResponses()) {
                            if (response.getPermissionName().equals(Manifest.permission.INTERNET)) {
                                // Internet permission denied
                                if (response.isPermanentlyDenied()) {
                                    // User selected "Never ask again" for Internet permission
                                    // Provide instructions on how to grant permission manually
                                }
                            } else if (response.getPermissionName().equals(Manifest.permission.ACCESS_WIFI_STATE)) {
                                // Access WiFi State permission denied
                                if (response.isPermanentlyDenied()) {
                                    // User selected "Never ask again" for Access WiFi State permission
                                    // Provide instructions on how to grant permission manually
                                }
                            } else if (response.getPermissionName().equals(Manifest.permission.ACCESS_NETWORK_STATE)) {
                                // Access Network State permission denied
                                if (response.isPermanentlyDenied()) {
                                    // User selected "Never ask again" for Access Network State permission
                                    // Provide instructions on how to grant permission manually
                                }
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        // Show rationale dialog if needed (optional)
                        token.continuePermissionRequest();
                    }
                })
                .check();



        //===================================================//



        return view;

    } //onCreate end()

    //=========================FUNCTION to send command==========================//

    public void sendCommand(String cmd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String command = "http://192.168.4.1/" + cmd;
                Log.d("Command------------------------------------------", command);
                Request request = new Request.Builder().url(command).build();
                try {
                    Response response = client.newCall(request).execute();
                    String myResponse = response.body().string();
                    final String cleanResponse = myResponse.replaceAll("\\<.*?\\>", ""); // remove HTML tags
                    cleanResponse.replace("\n", ""); // remove all new line characters
                    cleanResponse.replace("\r", ""); // remove all carriage characters
                    cleanResponse.replace(" ", ""); // removes all space characters
                    cleanResponse.replace("\t", ""); // removes all tab characters
                    cleanResponse.trim();
                    Log.d("Response  = ", cleanResponse);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtRES.setText(cleanResponse);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //=============================================================================//

}//Main end ()