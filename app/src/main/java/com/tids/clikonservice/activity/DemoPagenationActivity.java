package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmedelsayed.sunmiprinterutill.PrintMe;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tids.clikonservice.R;
import com.tids.clikonservice.activity.driver.DriversHomeActivity;
import com.tids.clikonservice.activity.merchant.StaffHomeActivity;
import com.tids.clikonservice.activity.technician.TechnicianHomeActivity;

public class DemoPagenationActivity extends AppCompatActivity {

    Button Collection, Transport, Technician;
    private PrintMe printMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_pagenation);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        printMe = new PrintMe(this);

        Collection=findViewById(R.id.btn_collection);
        Transport=findViewById(R.id.btn_Transport);
        Technician=findViewById(R.id.btn_Technician);

        Collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(DemoPagenationActivity.this, StaffHomeActivity.class);
//                startActivity(intent);
                generateQrCode("CK12345","CLIKON");
            }
        });
        Transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoPagenationActivity.this, DriversHomeActivity.class);
                startActivity(intent);
            }
        });
        Collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoPagenationActivity.this, TechnicianHomeActivity.class);
                startActivity(intent);
            }
        });

    }

    private void generateQrCode(String product_code,String product_name) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DemoPagenationActivity.this);
        alertDialogBuilder.setCancelable(true);
        LayoutInflater layoutInflater = LayoutInflater.from(DemoPagenationActivity.this);
        View popupInputDialogView = layoutInflater.inflate(R.layout.row_qrcode_generate_dialog_box, null);
        alertDialogBuilder.setView(popupInputDialogView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        ImageView iv_qr_code = popupInputDialogView.findViewById(R.id.zxing_barcode_scanner);
        TextView tv_product_name = popupInputDialogView.findViewById(R.id.tv_product_name);
        TextView tv_product_code = popupInputDialogView.findViewById(R.id.tv_product_code);
        AppCompatButton btn_print = popupInputDialogView.findViewById(R.id.btn_print);

        tv_product_name.setText(product_name);
        tv_product_code.setText(product_code);

        String text=product_code; // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv_qr_code.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        btn_print.setOnClickListener(v -> {
            alertDialog.cancel();
//            printQRCode(id,position);
            printLayout(product_code,product_name);
        });
    }

    private void printLayout(String product_code, String product_name) {
        View dialogView = LayoutInflater.from(DemoPagenationActivity.this).inflate(R.layout.row_qrcode_generate_dialog_box, null);

        ImageView iv_qr_code = dialogView.findViewById(R.id.zxing_barcode_scanner);
        TextView tv_product_name = dialogView.findViewById(R.id.tv_product_name);
        TextView tv_product_code = dialogView.findViewById(R.id.tv_product_code);
        AppCompatButton btn_print = dialogView.findViewById(R.id.btn_print);
        btn_print.setVisibility(View.GONE);

        tv_product_name.setText(product_name);
        tv_product_code.setText(product_code);

        String text=product_code; // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv_qr_code.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        printMe.sendViewToPrinter(dialogView);
    }

}