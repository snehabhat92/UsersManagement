package com.example.usersmanagement.View;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usersmanagement.Model.UserParcel;
import com.example.usersmanagement.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private static final String TAG = ProfileFragment.class.getCanonicalName();
    private static final int REQUEST_CODE = 101;
    private TextView txtPhone, txtEmail, txtWebsite, txtFirstName, txtLastName, txtUserName, txtAddress, txtCompany;
    private View view;
    private double lat, lng;
    private MapView mapView;
    private Button btnSave;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        hideKeyboard(getActivity());
        if(getActivity() != null) {
            getActivity().setTitle(getResources().getString(R.string.profile));
        }
        initialiseUI();
        return view;
    }

    private void hideKeyboard(Context context) {
        if(context == null || getActivity() == null) return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view == null) {
            view = new View(context);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initialiseUI() {
        mapView = view.findViewById(R.id.map);
        if(mapView != null) {
            mapView.onCreate(null);

            mapView.onResume();

            mapView.getMapAsync(this);
        }
        txtFirstName = view.findViewById(R.id.txt_first_name);
        txtLastName = view.findViewById(R.id.txt_last_name);
        txtUserName = view.findViewById(R.id.txt_user_name);
        txtPhone = view.findViewById(R.id.txt_phone);
        ImageView imgPhone = view.findViewById(R.id.img_phone);
        txtEmail = view.findViewById(R.id.txt_email);
        ImageView imgEmail = view.findViewById(R.id.img_email);
        txtAddress = view.findViewById(R.id.txt_address);
        txtWebsite = view.findViewById(R.id.txt_website);
        ImageView imgWebsite = view.findViewById(R.id.img_website);
        txtCompany = view.findViewById(R.id.txt_company);
        btnSave = view.findViewById(R.id.btn_save);
        progressBar = view.findViewById(R.id.progress_bar);
        setUpData();
        imgPhone.setOnClickListener(this);
        imgEmail.setOnClickListener(this);
        imgWebsite.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    private void setUpData() {
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey("User")) {
            UserParcel userParcel = bundle.getParcelable("User");
            if(userParcel != null) {
                String firstName = userParcel.getFirstName();
                if(!TextUtils.isEmpty(firstName) && txtFirstName != null) {
                    txtFirstName.setText(firstName);
                }
                String lastName = userParcel.getLastName();
                if(!TextUtils.isEmpty(lastName) && txtLastName != null) {
                    txtLastName.setText(lastName);
                }
                String UserName = userParcel.getUserName();
                if(!TextUtils.isEmpty(UserName) && txtUserName != null) {
                    txtUserName.setText(UserName);
                }
                String phone = userParcel.getPhone();
                if(!TextUtils.isEmpty(phone) && txtPhone != null) {
                    txtPhone.setText(PhoneNumberUtils.formatNumber(phone, "US"));
                }
                String email = userParcel.getEmail();
                if(!TextUtils.isEmpty(email) && txtEmail != null) {
                    txtEmail.setText(email);
                }
                String website = userParcel.getWebsite();
                if(!TextUtils.isEmpty(website) && txtWebsite != null) {
                    txtWebsite.setText(website);
                }
                String company = userParcel.getCompany();
                if(!TextUtils.isEmpty(company) && txtCompany != null) {
                    txtCompany.setText(company);
                }
                String address = userParcel.getAddress();
                if(!TextUtils.isEmpty(address) && txtAddress != null) {
                    txtAddress.setText(address);
                }
                lat = userParcel.getLat();
                lng = userParcel.getLng();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        handleSaveButton();
    }

    private void handleSaveButton() {
        if(getActivity() == null || TextUtils.isEmpty(txtPhone.getText().toString())) {
            btnSave.setVisibility(View.GONE);
            return;
        }
        if(!isContactAccessible()) {
            btnSave.setVisibility(View.GONE);
            return;
        }
        String phone = PhoneNumberUtils.normalizeNumber(txtPhone.getText().toString());
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = getActivity().getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                cur.close();
                btnSave.setVisibility(View.GONE);
            }
        } finally {
            if (cur != null)
                cur.close();
        }
    }

    private boolean isContactAccessible() {
        if(getActivity() == null) return false;
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, REQUEST_CODE);
            return false;
        }
        btnSave.setVisibility(View.VISIBLE);
        return true;
    }

    private void showPermissionDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == REQUEST_CODE) {
            String permission = permissions[0];
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                boolean showRationale = shouldShowRequestPermissionRationale(permission);
                if (!showRationale) {
                    showPermissionDialog(getResources().getString(R.string.permission_to_save_contact));
                } else if (Manifest.permission.WRITE_CONTACTS.equals(permission) || Manifest.permission.READ_CONTACTS.equals(permission)) {
                    //showRationale(permission, R.string.permission_denied_contacts);
                    showPermissionDialog(getResources().getString(R.string.provide_permission));
                }
            } else {
                btnSave.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v != null) {
            switch (v.getId()) {
                case R.id.img_phone:
                    launchActivity(v.getId());
                    break;
                case R.id.img_email:
                    launchActivity(v.getId());
                    break;
                case R.id.img_website:
                    launchActivity(v.getId());
                    break;
                case R.id.btn_save:
                    saveContact();
                    break;
            }
        }
    }

    private void launchActivity(int id) {
        Intent intent = null;
        if (id == R.id.img_phone) {
            intent = new Intent(Intent.ACTION_DIAL);
            if(!TextUtils.isEmpty(txtPhone.getText().toString())) {
                intent.setData(Uri.parse("tel:" + txtPhone.getText().toString()));
            }
        } else if(id == R.id.img_email) {
            String email = "";
            if(!TextUtils.isEmpty(txtEmail.getText().toString())) {
                email = txtEmail.getText().toString();
            }
            intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        }
        else {
            String website = "";
            if(!TextUtils.isEmpty(txtWebsite.getText().toString())) {
                website = txtWebsite.getText().toString();
            }
            intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", website);
        }
        if(getActivity() != null && getActivity().getPackageManager() != null && intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void saveContact(){
        progressBar.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.GONE);
        insertContact(txtUserName.getText().toString(), txtPhone.getText().toString(), txtEmail.getText().toString(), txtWebsite.getText().toString());
        progressBar.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        Toast.makeText(getActivity(), getResources().getString(R.string.contact_saved_successfully), Toast.LENGTH_SHORT).show();
    }

    private void insertContact(String displayName, String phoneNumber, String email, String website) {
        ArrayList<ContentProviderOperation> ops = new ArrayList <ContentProviderOperation> ();
        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());
        if (!TextUtils.isEmpty(displayName)) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            displayName).build());
        }

        if (!TextUtils.isEmpty(phoneNumber)) {
            String mobileNumber = PhoneNumberUtils.normalizeNumber(phoneNumber);
            if(!TextUtils.isEmpty(mobileNumber)) {
                ops.add(ContentProviderOperation.
                        newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
            }
        }

        if (!TextUtils.isEmpty(email)) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }
        if(!TextUtils.isEmpty(website)) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Website.URL, website)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Website.TYPE_WORK)
                    .build());
        }
        try {
            getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Marker"));
        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 0));

    }

    @Override
    public void onDestroy() {
        if(mapView != null) {
            mapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mapView != null)
            mapView.onResume();
    }

    @Override
    public void onPause() {
        if(mapView != null)
            mapView.onPause();
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(mapView != null)
            mapView.onLowMemory();
    }
}
