package com.rodikenya.rodiseriou.Service;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentInfo;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class PrintDocumentAdaptor extends android.print.PrintDocumentAdapter {
    Context context;
    String path;

    public PrintDocumentAdaptor(Context context, String path) {
        this.context = context;
        this.path = path;
    }


    @Override
    public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes1, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
         if(cancellationSignal.isCanceled())
         {
             layoutResultCallback.onLayoutCancelled();
         }else
         {
             PrintDocumentInfo.Builder builder=new PrintDocumentInfo.Builder("file name");
             builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                     .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                     .build();
             layoutResultCallback.onLayoutFinished(builder.build(),!printAttributes1.equals(printAttributes));
         }
    }

    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
        InputStream in=null;
        OutputStream out=null;
        try{
            File file=new File(path);
            in=new FileInputStream(file);
            out=new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
            byte[] buff=new byte[16384];
            int size;
            while((size=in.read(buff))>=0 && !cancellationSignal.isCanceled())
            {
                out.write(buff,0,size);
            }
            if(cancellationSignal.isCanceled())
            {
                writeResultCallback.onWriteCancelled();
            }else
            {
                writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
            }
        }catch (Exception Ex)
        {
          writeResultCallback.onWriteFailed(Ex.getMessage());
          Ex.printStackTrace();
        }
        finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error "+e.getMessage());
            }

        }
    }
}
