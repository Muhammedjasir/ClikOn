package com.tids.clikonservice.activity.merchant;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.AesBase64Wrapper;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.activity.LoginActivity;
import com.tids.clikonservice.activity.technician.TechnicianProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class MerchantProfileActivity extends AppCompatActivity {

    private PrefManager pref;
    private SharedPreferences sp;

    private String authorization="",mediaPath="",technicianID="",user_name="";

    private ImageView iv_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        pref = new PrefManager(getApplicationContext());
        authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");
        technicianID = sp.getString(Constant.USER_USERID,"");

        user_name = sp.getString(Constant.USER_USERNAME, "");

        iv_profile = findViewById(R.id.iv_profile);
        EditText tv_name = findViewById(R.id.tv_name);

        String user_profile = sp.getString(Constant.USER_PROFILE, "");
        if (!user_profile.equalsIgnoreCase("") || user_profile != null){
            Glide.with(getApplicationContext())
                    .load(user_profile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
//                    .placeholder(R.drawable.ic_profile)
//                    .error(R.drawable.ic_profile)
                    .into(iv_profile);
        }
        tv_name.setText(user_name);

        ImageView ivBack=findViewById(R.id.back_btn);
        ivBack.setOnClickListener(v -> onBackPressed());

        findViewById(R.id.change_password).setOnClickListener(v -> {
            setChangePassword();
        });

        iv_profile.setOnClickListener(v -> {
            ImageSelectActivity.startImageSelectionForResult(this, true, true, true, true, 1213);
        });

        findViewById(R.id.iv_logout).setOnClickListener(v -> {
            setlogout();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 1213 && resultCode == RESULT_OK && null != data) {

                mediaPath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
                Bitmap selectedImage = BitmapFactory.decodeFile(mediaPath);
                iv_profile.setImageBitmap(selectedImage);

                uploadProfilePhoto();
            }
        } catch (Exception e) {

            customToast("something wrong");
        }
    }

    private void uploadProfilePhoto() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SM_STS_CODE","SERVSRT");
            jsonObject.put("SM_SRP_SYS_ID",technicianID);

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void setChangePassword() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MerchantProfileActivity.this);
        alertDialogBuilder.setCancelable(false);
        LayoutInflater layoutInflater = LayoutInflater.from(MerchantProfileActivity.this);
        View popupInputDialogView = layoutInflater.inflate(R.layout.row_change_password_dialog_box, null);
        alertDialogBuilder.setView(popupInputDialogView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Button bt_Save = popupInputDialogView.findViewById(R.id.btn_login);
        Button btn_cancel = popupInputDialogView.findViewById(R.id.btn_cancel);
        TextInputEditText tv_old_password = popupInputDialogView.findViewById(R.id.tv_old_password);
        TextInputEditText tv_new_password = popupInputDialogView.findViewById(R.id.tv_new_password);
        TextInputEditText tv_confirm_password = popupInputDialogView.findViewById(R.id.tv_confirm_password);
        bt_Save.setOnClickListener(v -> {

            try {
                String old_pass = tv_old_password.getText().toString().trim();
                String new_password = tv_new_password.getText().toString().trim();
                String confirm_password = tv_confirm_password.getText().toString().trim();

                String old_password_decrypt = sp.getString(Constant.USER_PROFILE, "");
                String old_password = AesBase64Wrapper.decodeAndDecrypt(old_password_decrypt,user_name);

                if (!old_pass.equalsIgnoreCase(old_password)){
                    customToast("Invalid old password");
                }else if (new_password.isEmpty() || confirm_password.isEmpty()){
                    customToast("Set password");
                }else if (!new_password.equalsIgnoreCase(confirm_password)){
                    customToast("Check password");
                }else {
                    uploadNewPassword(new_password);
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        });
        btn_cancel.setOnClickListener(v -> {
            alertDialog.cancel();
        });
    }
    private void uploadNewPassword(String new_password) {
        try {
            String decodeData = AesBase64Wrapper.encryptAndEncode(new_password,user_name);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("USER_PWD",decodeData);

            AndroidNetworking.put(Constant.BASE_URL + Constant.TECHNICIAN_USER + "/" +
                    technicianID)
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response::",response.toString());

                            try {
                                if (response.getBoolean("status")) {
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString(Constant.USER_PASSWORD, decodeData);
                                    editor.apply();
                                }else {
                                    customToast(response.getString("message"));
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            showError(anError);
                        }
                    });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void setlogout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MerchantProfileActivity.this);
        alertDialogBuilder.setCancelable(false);
        LayoutInflater layoutInflater = LayoutInflater.from(MerchantProfileActivity.this);
        View popupInputDialogView = layoutInflater.inflate(R.layout.row_logout_alert, null);
        alertDialogBuilder.setView(popupInputDialogView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Button btyes = popupInputDialogView.findViewById(R.id.btYes);
        Button btcancel = popupInputDialogView.findViewById(R.id.btNo);
        btyes.setOnClickListener(v -> {
            alertDialog.cancel();
            SharedPreferences.Editor ed = sp.edit();
            ed.clear();
            ed.apply();
            pref.setLogin(false);
            pref.setLoginFlag(false);

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });
        btcancel.setOnClickListener(v -> {
            alertDialog.cancel();
        });
    }
    private void showError(ANError anError) {
        Toast.makeText(MerchantProfileActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        Log.e("Error :: ", anError.getErrorBody());
    }

    private void customToast(String message){
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.colorPrimary)).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}