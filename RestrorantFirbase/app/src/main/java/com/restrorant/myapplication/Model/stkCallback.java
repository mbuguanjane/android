package com.restrorant.myapplication.Model;

import java.util.List;

public class stkCallback {
    private String CheckoutRequestID,MerchantRequestID,ResultDesc;
    private  long ResultCode;
    //private CallbackMetadata callbackMetadata;
    private List<Itemm> itemmList;

    public stkCallback() {
    }

    public stkCallback(String checkoutRequestID, String merchantRequestID, String resultDesc, long resultCode, List itemmList) {
        CheckoutRequestID = checkoutRequestID;
        MerchantRequestID = merchantRequestID;
        ResultDesc = resultDesc;
        ResultCode = resultCode;
        this.itemmList = itemmList;
    }

    public String getCheckoutRequestID() {
        return CheckoutRequestID;
    }

    public void setCheckoutRequestID(String checkoutRequestID) {
        CheckoutRequestID = checkoutRequestID;
    }

    public String getMerchantRequestID() {
        return MerchantRequestID;
    }

    public void setMerchantRequestID(String merchantRequestID) {
        MerchantRequestID = merchantRequestID;
    }

    public String getResultDesc() {
        return ResultDesc;
    }

    public void setResultDesc(String resultDesc) {
        ResultDesc = resultDesc;
    }

    public long getResultCode() {
        return ResultCode;
    }

    public void setResultCode(long resultCode) {
        ResultCode = resultCode;
    }

    public List<Itemm> getItemmList() {
        return itemmList;
    }

    public void setItemmList(List itemmList) {
        this.itemmList = itemmList;
    }
}
