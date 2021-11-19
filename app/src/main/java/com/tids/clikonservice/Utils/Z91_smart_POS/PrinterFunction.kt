//package com.tids.techorbit.utils
//
//import android.content.Context
//import android.util.Log
//import com.tids.techorbit.R
//import com.tids.techorbit.model.Data
//import com.tids.techorbit.model.reports.inventory.Inventory
//import com.tids.techorbit.model.reports.vendor.ModelVendror
//
//class PrinterFunction(val context: Context) {
//
//    fun call_device_detail_printer_bluetooth() {
//        try {
//            lateinit var s_builder: java.lang.StringBuilder
//            s_builder = StringBuilder()
//            s_builder.append("Name  : " + TechorbitSharedPreference(context).getValue(KEYS.NAME))
//            s_builder.append("\n")
//            s_builder.append("Mobile: " + TechorbitSharedPreference(context).getValue(KEYS.MOBILE))
//            s_builder.append("\n")
//            s_builder.append("\n")
//            s_builder.append("\n")
//            s_builder.append("\n")
//
//
//            BluetoothUtil.sendTest(s_builder.toString().toByteArray())
//        } catch (e: Exception) {
//            Log.d("Exception", e.message!!)
//        }
//    }
//
//    fun call_inventory_report_receipt_printer_bluetooth(inventoryList: List<Inventory>) {
//
//        try {
//            if (inventoryList.isNotEmpty()) {
//                val stringHandler = StringHandler()
//                val s_builder = java.lang.StringBuilder()
//                val current_dateTime = Current_DateTime(context)
//                s_builder.append("\tInventory Report")
//                s_builder.append(context.resources.getString(R.string.new_line))
//                //                s_builder.append("Owner: "+sessionManager.getUsername()+"\t\t"+sessionManager.getDistributorCode());
////                s_builder.append(
////                    "Owner: " + sessionManager.get_Shopname()
////                        .toString() + "\t\t" + sessionManager.getDistributorCode()
////                )
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append("Terminal ID: " + TechorbitSharedPreference(context).getValue(KEYS.M2MID))
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append(
//                    "Current Wallet: " + TechorbitSharedPreference(context).getValue(
//                        KEYS.BALANCE
//                    )
//                )
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append("Date&Time: " + current_dateTime.current_datetimefor_receiptPrint())
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append(context.resources.getString(R.string.separator_line))
//                s_builder.append(
//                    stringHandler.custom_download_receipt_text(
//                        "Product",
//                        15
//                    ) + stringHandler.download_receipt_text("Value") + stringHandler.download_receipt_text(
//                        "Stock"
//                    )
//                )
//                s_builder.append(context.resources.getString(R.string.new_line))
//                for (i in inventoryList.indices) {
//                    s_builder.append(
//                        stringHandler.custom_download_receipt_text(
//                            inventoryList[i].name, 15
//                        ) + stringHandler.download_receipt_text(
//                            inventoryList[i].faceValue
//                        ) + stringHandler.download_receipt_text(inventoryList[i].stock)
//                    )
//                    s_builder.append(context.resources.getString(R.string.new_line))
//                }
//                s_builder.append(context.resources.getString(R.string.end_line))
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append(context.resources.getString(R.string.new_line))
//                BluetoothUtil.sendData(s_builder.toString().toByteArray())
//            } else {
////                val popUpHandler = PopUpHandler(context)
////                val popup_dialog: Dialog =
////                    popUpHandler.normal_popup("Sorry", "No Data To Print ")
////                popup_dialog.show()
//            }
//        } catch (e: java.lang.Exception) {
//            Log.e("Exception", e.message!!)
//        }
//    }
//
//    fun call_recharge_receipt_printer_bluetooth(
//        tid: String,
//        beneficiary: String,
//        reh_amt: String,
//        pyb_amt: String,
//        status: String,
//        country: String
//    ) {
//        try {
//            val current_dateTime = Current_DateTime(context)
//            val s_builder = java.lang.StringBuilder()
//            s_builder.append("Date Time: " + current_dateTime.current_datetimefor_receiptPrint())
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Terminal ID: " + TechorbitSharedPreference(context).getValue(KEYS.M2MID))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("TID: $tid")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Country: $country")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Beneficiary Num: $beneficiary")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Recharge Amount: $reh_amt")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Amount Payable: $pyb_amt")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Status: $status")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Thank you for recharging at \nTechorbit pos. For queries and \ncomplaints kindly contact \nour customer care service.")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Customer Care")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("\tPhone: +9714 8800 800")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("\tPhone: +9714 8800 855")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.end_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            BluetoothUtil.sendData(s_builder.toString().toByteArray())
//        } catch (e: java.lang.Exception) {
//        }
//    }
//
//    fun call_scratchcaerd_printer_bluetooth(
//        type: String,
//        currency: String,
//        amount: String,
//        ben_number: String?,
//        decoded_rechargeCardNumber: String,
//        transId: String,
//        transactionCode: String,
//        duplicate: String
//    ) {
//        try {
////            Log.d("name", sessionManager.getName())
////            Log.d("location", sessionManager.getLocation())
////            Log.d("mobile", sessionManager.getMobile())
//            val s_builder = java.lang.StringBuilder()
//            val s_builder_header = java.lang.StringBuilder()
//            val current_dateTime = Current_DateTime(context)
//            val name = type.substring(0, type.indexOf(" "))
//
////            s_builder_header.append("\t"+type.toUpperCase());
//            s_builder_header.append(name.toUpperCase() + " " + "Evoucher")
//            s_builder_header.append(context.resources.getString(R.string.new_line))
//            s_builder_header.append(currency.toUpperCase() + " " + amount)
//            s_builder_header.append(context.resources.getString(R.string.new_line))
//            //            s_builder_header.append("\t"+currency+" "+amount);
////            s_builder_header.append(context.getResources().getString(R.string.new_line));
//            s_builder_header.append("Pin Number")
//            s_builder_header.append(context.resources.getString(R.string.new_line))
//            //            s_builder_header.append(context.getResources().getString(R.string.star_line_separator));
//            s_builder_header.append(decoded_rechargeCardNumber)
//            s_builder_header.append(context.resources.getString(R.string.new_line))
//            s_builder_header.append(duplicate)
//
//            s_builder_header.append(context.resources.getString(R.string.new_line))
//            //            s_builder.append(context.getResources().getString(R.string.star_line_separator));
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(
//                "Merchant         : " + TechorbitSharedPreference(context).getValue(KEYS.SHOPENAME)
//            )
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Location         : " + 0)
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(
//                "Phone No         : " + TechorbitSharedPreference(context).getValue(
//                    KEYS.MOBILE
//                )
//            )
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(
//                "Terminal ID      : " + TechorbitSharedPreference(context).getValue(
//                    KEYS.M2MID
//                )
//            )
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Receipt No       : $transId")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Transaction Code : $transactionCode")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Date & Time      : " + current_dateTime.current_datetimefor_receiptPrint())
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            when {
//                type.toLowerCase() == "salik e-voucher" -> {
//                    s_builder.append(context.resources.getString(R.string.salik_note))
//                }
//                type.toLowerCase() == "hello e-voucher" -> {
//                    s_builder.append(context.resources.getString(R.string.hello_note))
//                }
//                type.toLowerCase() == "five e-voucher" -> {
//                    s_builder.append(context.resources.getString(R.string.five_note))
//                }
//                type.toLowerCase() == "etisalat e-voucher" -> {
//                    s_builder.append(context.resources.getString(R.string.etisalat_note))
//                }
//                type.toLowerCase() == "du voucher" -> {
//                    s_builder.append(context.resources.getString(R.string.du_note))
//                }
//                type.toLowerCase() == "virigin e-voucher" -> {
//                    s_builder.append(context.resources.getString(R.string.viring_note))
//
//                }
//
//                //            bluetoothUtil.sendData(1, s_builder.toString().getBytes());
//            }
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.end_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            //            bluetoothUtil.sendData(1, s_builder.toString().getBytes());
//            BluetoothUtil.printData(
//                s_builder_header.toString().toByteArray(),
//                s_builder.toString().toByteArray()
//            )
//        } catch (e: java.lang.Exception) {
//            Log.d("Exception", e.message!!)
//        }
//    }
//
//    fun call_transaction_report_receipt_printer_bluetooth(
//        transaction_list: List<Data>,
//        from: String,
//        to: String,
//        startTime: String,
//        endTime: String,
//        rechargeAmount: Float,
//        commissionAmount: Float
//    ) {
//        val s_builder = java.lang.StringBuilder()
//        val current_dateTime = Current_DateTime(context)
//        try {
//            s_builder.append("\tDetailed Report")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            //            s_builder.append("Owner: "+sessionManager.getUsername()+"\t\t"+sessionManager.getDistributorCode());
//            s_builder.append(
//                "Owner: " + TechorbitSharedPreference(context).getValue(KEYS.SHOPENAME)
//                    .toString() + "\t\t" + TechorbitSharedPreference(context).getValue(KEYS.DISTRIBUTERCODE)
//            )
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Terminal ID: " + TechorbitSharedPreference(context).getValue(KEYS.M2MID))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Current Wallet: " + TechorbitSharedPreference(context).getValue(KEYS.BALANCE))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Date&Time: " + current_dateTime.current_datetimefor_receiptPrint())
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Reporting Period")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("$from $startTime")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("$to $endTime")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.separator_line))
//
////            float tot_mrp = 0;
////            float tot_commission = 0;
//            Log.d("b4", "loop")
//            val temp_date: String? = null
//            for (i in transaction_list.indices) {
//                val date_time_array: Array<String> =
//                    current_dateTime.datetime_split(transaction_list[i].createdTime)
//                s_builder.append("Date,Time  : " + transaction_list[i].createdTime)
//                s_builder.append(context.resources.getString(R.string.new_line))
////                if (transaction_list[i].getService().equals("Pin Download")) {
////                    s_builder.append("Type       : PIN Download")
////                    s_builder.append(context.resources.getString(R.string.new_line))
////                    s_builder.append("Product    : " + transaction_list[i].operator)
////                    s_builder.append(context.resources.getString(R.string.new_line))
////                    //                    s_builder.append("Denomination    : "+transaction_list.get(i).getMrp());
////                    s_builder.append(
////                        "Denom      : " + java.lang.String.format(
////                            "%.2f",
////                            transaction_list[i].rechargeAmount
////                        )
////                    )
////                    s_builder.append(context.resources.getString(R.string.new_line))
////                    s_builder.append(
////                        "Quantity   : " + transaction_list[i].beneficiaryNumber
////                    )
////                    s_builder.append(context.resources.getString(R.string.new_line))
////                    val value: Double =
////                        transaction_list[i].rechargeAmount!!
////                    //                    s_builder.append("Value    : AED "+value);
////                    s_builder.append("Value      : AED " + String.format("%.2f", value))
////                    s_builder.append(context.resources.getString(R.string.new_line))
////                    s_builder.append(context.resources.getString(R.string.new_line))
////                    s_builder.append(context.resources.getString(R.string.new_line))
////                }
//
//
//                s_builder.append("Product    : " + transaction_list[i].operator)
//                s_builder.append(context.resources.getString(R.string.new_line))
//                if (!transaction_list[i].beneficiaryNumber.equals("")) {
//                    s_builder.append(
//                        "Beneficiary: " + transaction_list[i].beneficiaryNumber
//                    )
//                    s_builder.append(context.resources.getString(R.string.new_line))
//                }
//                //                    s_builder.append("Amount     : "+transaction_list.get(i).getMrp());
//                s_builder.append(
//                    "Amount     : " + java.lang.String.format(
//                        "%.2f",
//                        transaction_list[i].rechargeAmount
//                    )
//                )
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append("Status     : " + transaction_list[i].rechargeStatus)
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append(context.resources.getString(R.string.new_line))
//
//            }
//            s_builder.append(context.resources.getString(R.string.separator_line))
//            //            s_builder.append("\t"+"Total mrp: "+tot_mrp+"\n");
//
////            s_builder.append("Total Face Value: "+rechargeAmount+"\n");
//            s_builder.append(
//                "Total Face Value: " + String.format(
//                    "%.2f",
//                    rechargeAmount
//                ) + "\n"
//            )
//            //            s_builder.append("\t"+"Total Commission: "+tot_commission+"\n");
//            s_builder.append(
//                "Total Commission: " + String.format(
//                    "%.2f",
//                    commissionAmount
//                ) + "\n"
//            )
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.end_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            Log.d("s_builder", s_builder.toString())
//            BluetoothUtil.sendData(s_builder.toString().toByteArray())
//        } catch (e: java.lang.Exception) {
//            Log.e("Exception", e.message!!)
//        }
//    }
//
//    fun call_OTAR_receipt_printer_bluetooth(
//        transId: String,
//        beneficiaryNumber: String,
//        reh_amt: String,
//        operator: String,
//        status: String
//    ) {
//        try {
//            val current_dateTime = Current_DateTime(context)
//            val s_builder = java.lang.StringBuilder()
//            s_builder.append("Date Time: " + current_dateTime.current_datetimefor_receiptPrint())
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Terminal ID: " + TechorbitSharedPreference(context).getValue(KEYS.M2MID))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("TID: $transId")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Beneficiary Num: $beneficiaryNumber")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Operator       : $operator")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Recharge Amount: $reh_amt")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Status: $status")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Thank you for recharging at \nTechorbit pos. For queries and \ncomplaints kindly contact \nour customer care service.")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            when (operator) {
//                "Otar" -> s_builder.append("Customer Care: 800101")
//                "Du" -> s_builder.append("Customer Care: 800155")
//            }
//
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.end_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            BluetoothUtil.sendData(s_builder.toString().toByteArray())
//        } catch (e: java.lang.Exception) {
//        }
//    }
//
//    fun call_purchase_report_receipt_printer_bluetooth(
//        transactionList: ArrayList<com.tids.techorbit.model.purchase.Data>,
//        from: String?,
//        to: String?,
//        total_credit_amount: Float,
//        total_debit_amount: Float
//    ) {
//        try {
//            if (transactionList.size > 0) {
//                var s_builder = java.lang.StringBuilder()
//                var current_dateTime = Current_DateTime(context)
//                s_builder.append("\tPurchase Report")
//                s_builder.append(context.resources.getString(R.string.new_line))
//                //                s_builder.append("Owner: "+sessionManager.getUsername()+"\t\t"+sessionManager.getDistributorCode());
//                s_builder.append(
//                    "Owner: " + TechorbitSharedPreference(context).getValue(KEYS.SHOPENAME)
//                        .toString() + "\t\t" + TechorbitSharedPreference(context).getValue(KEYS.DISTRIBUTERCODE)
//                )
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append("Terminal ID: " + TechorbitSharedPreference(context).getValue(KEYS.M2MID))
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append(
//                    "Current Wallet: " + TechorbitSharedPreference(context).getValue(
//                        KEYS.BALANCE
//                    )
//                )
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append("Date&Time: " + current_dateTime.current_datetimefor_receiptPrint())
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append("Reporting Period")
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append(from)
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append(to)
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append(context.resources.getString(R.string.separator_line))
//
////                String temp_date = null;
////                int token = 0;
////                float total_credit_amount = 0, total_debit_amount = 0;
//                for (i in transactionList.indices) {
//
////                    String[] date_time_array = current_dateTime.datetime_split(transactionList.get(i).getDate());
////                    if(!date_time_array[0].equals(temp_date)){
////                        s_builder.append(date_time_array[0]);
////                        s_builder.append(context.getResources().getString(R.string.new_line));
////                        token = 0;
////                        temp_date = date_time_array[0];
////                    }
//
//
////                    token = token+1;
//                    var credit_amount = 0f
//                    var debit_amount = 0f
//
//
////                    s_builder.append("# "+token+"\n\t\t\t\t\t\t"+date_time_array[1]);
//                    s_builder.append("Date,Time: " + transactionList[i].createdTime)
//                    s_builder.append(context.resources.getString(R.string.new_line))
//                    s_builder.append("Narration: " + transactionList[i].narration)
//                    s_builder.append(context.resources.getString(R.string.new_line))
//                    //                s_builder.append("\t\tCredit\t\tDebit");
//                    s_builder.append("\tCredit\tDebit")
//                    s_builder.append(context.resources.getString(R.string.new_line))
//                    Log.d("Type", transactionList[i].transactionType)
//                    if (transactionList[i].transactionType.equals("Credit")) {
//                        credit_amount = transactionList[i].amount.toFloat()
//                        //                        total_credit_amount = total_credit_amount + credit_amount;
//                    } else {
//                        debit_amount = transactionList[i].amount.toFloat()
//                        //                        total_debit_amount = total_debit_amount + debit_amount;
//                    }
//                    //                s_builder.append("\t\t"+credit_amount+"\t\t"+debit_amount);
////                    s_builder.append("\t"+credit_amount+"\t"+debit_amount);
//                    val ca = String.format("%.02f", credit_amount)
//                    val da = String.format("%.02f", debit_amount)
//                    s_builder.append("\t" + ca + "\t" + da)
//                    s_builder.append(context.resources.getString(R.string.new_line))
//                    s_builder.append(context.resources.getString(R.string.new_line))
//                }
//                s_builder.append(context.resources.getString(R.string.separator_line))
//                val tca = String.format("%.02f", total_credit_amount)
//                val tda = String.format("%.02f", total_debit_amount)
//                s_builder.append("Total Credit: $tca\n")
//                s_builder.append("Total Debit : $tda\n")
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append(context.resources.getString(R.string.end_line))
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append(context.resources.getString(R.string.new_line))
//                BluetoothUtil.sendData(s_builder.toString().toByteArray())
//            }
//        } catch (e: java.lang.Exception) {
//        }
//    }
//
//    fun call_report_receipt_printer_bluetooth(
//        data: ArrayList<com.tids.techorbit.model.reciept.Data>, from: String?, to: String?
//    ) {
//        try {
////            val convertionProcess = ConvertionProcess()
//            var s_builder = java.lang.StringBuilder()
//            var current_dateTime = Current_DateTime(context)
//            s_builder.append("\tReceipt Report")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            //            s_builder.append("Owner: "+sessionManager.getUsername()+"\t\t"+sessionManager.getDistributorCode());
//            s_builder.append(
//                "Owner: " + TechorbitSharedPreference(context).getValue(KEYS.SHOPENAME)
//                    .toString() + "\t\t" + TechorbitSharedPreference(context).getValue(KEYS.DISTRIBUTERCODE)
//            )
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Terminal ID: " + TechorbitSharedPreference(context).getValue(KEYS.M2MID))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Current Wallet: " + TechorbitSharedPreference(context).getValue(KEYS.BALANCE))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Date&Time: " + current_dateTime.current_datetimefor_receiptPrint())
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Reporting Period")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(from)
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(to)
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.separator_line))
//
//            var total_amount: Double = 0.0
//            for (i in data) {
//
//
//                total_amount = total_amount + i.amount.toFloat()
//
//                s_builder.append(i.date + "\n")
//                s_builder.append("Receipt ID      :${i.receiptId}\n")
//                s_builder.append("paid To         :${i.paidTo}\n")
//                s_builder.append("DistributorCode :${i.distributorCode}\n")
//                s_builder.append("Amount          :${i.amount} \n")
//                s_builder.append("Mode            :${i.mode}\n")
//                s_builder.append("Service         :${i.service}\n ")
//                s_builder.append(context.resources.getString(R.string.new_line))
//            }
//            s_builder.append(context.resources.getString(R.string.separator_line))
//            s_builder.append(
//                "Total Amount: " + total_amount
//            )
//            s_builder.append(context.resources.getString(R.string.end_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            BluetoothUtil.sendData(s_builder.toString().toByteArray())
//        } catch (e: java.lang.Exception) {
//            Log.e("Exception", e.message!!)
//        }
//    }
//
//    fun call_venderout_receipt_printer_bluetooth(
//        modelVendror: ModelVendror,
//        from: String?,
//        to: String?
//    ) {
//        try {
//            var s_builder = java.lang.StringBuilder()
//            var current_dateTime = Current_DateTime(context)
//            s_builder.append("\tVendor Outstanding")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            //            s_builder.append("Owner: "+sessionManager.getUsername()+"\t\t"+sessionManager.getDistributorCode());
//            s_builder.append(
//                "Owner: " + TechorbitSharedPreference(context).getValue(KEYS.SHOPENAME)
//                    .toString() + "\t\t" + TechorbitSharedPreference(context).getValue(KEYS.DISTRIBUTERCODE)
//            )
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Terminal ID: " + TechorbitSharedPreference(context).getValue(KEYS.M2MID))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Current Wallet: " + TechorbitSharedPreference(context).getValue(KEYS.BALANCE))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Date&Time: " + current_dateTime.current_datetimefor_receiptPrint())
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Reporting Period")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(from)
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(to)
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.separator_line))
//
//            for (i in modelVendror.data) {
////                token = token+1;
//                s_builder.append("Date,Time: " + "${i.date}")
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append("Narration: " + "${i.narration}")
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append("\tPurchase\tReceipt")
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append("\t" + "${i.credit}" + "\t" + "${i.debit}")
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append(context.resources.getString(R.string.new_line))
//            }
//            s_builder.append(context.resources.getString(R.string.separator_line))
//            s_builder.append("OpeningBalance  : ${modelVendror.openingBalance}\n")
//            s_builder.append("ClosingBalance  : ${modelVendror.closingBalance}\n")
//            s_builder.append("Total Purchase  : ${modelVendror.totalCredit}\n")
//            s_builder.append("Total Receipt   : ${modelVendror.totalDebit}\n")
//
//
//
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.end_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            BluetoothUtil.sendData(s_builder.toString().toByteArray())
//        } catch (e: java.lang.Exception) {
//            Log.e("Exception", e.message!!)
//        }
//    }
//
//    fun call_sales_report_receipt_printer_bluetooth(head:String,
//                                                    salesList: List<com.tids.techorbit.model.reports.terminal.Data>,
//                                                    from: String?,
//                                                    to: String?,
//                                                    total_facevalue: String,
//                                                    totalcommission: String
//    ) {
//        try {
//
//            val s_builder = java.lang.StringBuilder()
//            val current_dateTime = Current_DateTime(context)
//            s_builder.append("\t$head")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            //                s_builder.append("Owner: "+sessionManager.getUsername()+"\t\t"+sessionManager.getDistributorCode());
//            s_builder.append(
//                "Owner: " + TechorbitSharedPreference(context).getValue(KEYS.SHOPENAME)
//                    .toString() + "\t\t" + TechorbitSharedPreference(context).getValue(
//                    KEYS.DISTRIBUTERCODE
//                )
//            )
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Terminal ID: " + TechorbitSharedPreference(context).getValue(KEYS.M2MID))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Current Wallet: " + TechorbitSharedPreference(context).getValue(KEYS.BALANCE))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Date&Time: " + current_dateTime.current_datetimefor_receiptPrint())
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Reporting Period")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("$from")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("$to")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.separator_line))
//
//            for (i in salesList) {
//                s_builder.append(i.serviceName)
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append("Count   Face Value  Commission ")
//                s_builder.append(context.resources.getString(R.string.new_line))
//
//                s_builder.append("${i.totalCount}\t${i.totalMRP}\t${i.totalCommission}")
//                s_builder.append(context.resources.getString(R.string.new_line))
//                s_builder.append(context.resources.getString(R.string.new_line))
//            }
//            s_builder.append(context.resources.getString(R.string.separator_line))
////            s_builder.append("size of the list is ${salesList.size}")
//            //                s_builder.append("\t\t"+"Total mrp: "+tot_mrp+"\n");
//            s_builder.append("Total Face Value: $total_facevalue\n")
//            s_builder.append("Total Commission: $totalcommission\n")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.end_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//
//            BluetoothUtil.sendData(s_builder.toString().toByteArray())
//            Log.d("TAG", "Printing.....")
//
//        } catch (e: java.lang.Exception) {
//        }
//    }
//
//
//    fun gameCardPrint(amount: String, pincode: String) {
//        try {
//            val current_dateTime = Current_DateTime(context)
//            val s_builder = java.lang.StringBuilder()
//
//            s_builder.append("\tGoogle UAE $amount AED")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("\tPinCode")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("\t$pincode")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.separator_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("TerminalId:${TechorbitSharedPreference(context).getValue(KEYS.M2MID)}")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Google TerminalID:${TechorbitSharedPreference(context).getValue(KEYS.M2MID)}")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Merchant Name:${TechorbitSharedPreference(context).getValue(KEYS.USERNAME)}")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Merchant ID:${TechorbitSharedPreference(context).getValue(KEYS.USERNAME)}")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Date/Time:${current_dateTime.current_datetimefor_receiptPrint()}")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("TAX ID:TEST123")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.separator_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("VALUE:$amount")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append("Serial No:${TechorbitSharedPreference(context).getValue(KEYS.M2MID)}")
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(context.resources.getString(R.string.separator_line))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            s_builder.append(
//                "13+ y.o. Restrictions apply. Please\n refer to\nfor full terms.\n لتحصيل القيمة يجب إدخال الرمز في\n تطبيق\nStore Play أو\n redeem/com.google.play\n يجب توفر حساب على Payments\nGoogle\n الرسوم المدفوعة غير قابلة للاسترداد.\nالبطاقة مناسبة فقط لسن 13 سنة وما\nفوق\nتنطبق عليها قيود. يرجى الاطلاععلى\nplay.google.com/uae-card-terms\n   لقراءة الشروط الكاملة.\n"
//
//
//            )
//            s_builder.append(s_builder.append(context.resources.getString(R.string.new_line)))
//            s_builder.append(context.resources.getString(R.string.new_line))
//            BluetoothUtil.sendData(s_builder.toString().toByteArray())
//
//
//        } catch (e: java.lang.Exception) {
//        }
//
//    }
//
//
//}