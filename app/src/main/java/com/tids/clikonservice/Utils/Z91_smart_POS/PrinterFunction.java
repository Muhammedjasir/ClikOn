package com.tids.clikonservice.Utils.Z91_smart_POS;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class PrinterFunction {

    public void printQR(String product_name, int sys_id, String product_code){

        try {
            String text= String.valueOf(sys_id); // Whatever you need to encode in the QR code
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {

                String content =
                        product_name + "\n" +
                                sys_id + " - " + product_code ;
                BluetoothUtil.sendTest(content.getBytes("UTF-8"));

                BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                byte[] bytes = BluetoothUtil.decodeBitmap(bitmap);
                BluetoothUtil.sendTest(bytes);

                String spacing = "\n\n";
                BluetoothUtil.sendTest(spacing.getBytes("UTF-8"));

            } catch (WriterException e) {
                e.printStackTrace();
            }

        }catch (Exception ex){
            Log.d("Exception", ex.toString());
        }
    }

}
