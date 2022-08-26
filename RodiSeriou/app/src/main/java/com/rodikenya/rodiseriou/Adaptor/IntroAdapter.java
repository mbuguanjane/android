package com.rodikenya.rodiseriou.Adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rodikenya.rodiseriou.MainActivity;
import com.rodikenya.rodiseriou.Model.Intro;
import com.rodikenya.rodiseriou.R;

import java.util.List;

public class IntroAdapter extends RecyclerView.Adapter<IntroAdapter.IntroViewHolder> {
    Context context;
    List<Intro> introList;

    public IntroAdapter(Context context, List<Intro> introList) {
        this.context = context;
        this.introList = introList;
    }

    public class IntroViewHolder extends RecyclerView.ViewHolder {
        TextView Title,SubTitle;
        Button description;
        LinearLayout linearLayout;
        RelativeLayout relativeLayout;
        ImageView fb,twitter,youtube,location,call,gmail;
        public IntroViewHolder(@NonNull View itemView) {
            super(itemView);
            Title=(TextView)itemView.findViewById(R.id.Title);
            gmail=(ImageView)itemView.findViewById(R.id.gmail);
            gmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Contact Us");
                    alertDialog.setMessage("Email: rodikenya@iconnect.co.ke");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
            fb=(ImageView)itemView.findViewById(R.id.facebook);
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/rodikenya/"));
                    context.startActivity(browserIntent);
                }
            });
            youtube=(ImageView)itemView.findViewById(R.id.youtube);
            youtube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com"));
                    context.startActivity(browserIntent);
                }
            });
            twitter=(ImageView)itemView.findViewById(R.id.twitter);
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/RODI_Kenya"));
                    context.startActivity(browserIntent);
                }
            });
            location=(ImageView)itemView.findViewById(R.id.location);
            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Our Location");
                    alertDialog.setMessage("We are located at Rodi Kenya Office \n and Rodi Kenya Centre ");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            });
            call=(ImageView)itemView.findViewById(R.id.contact);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Contacts");
                    alertDialog.setMessage("P.O Box 746-00232 Ruiru- Kenya . \n" +
                            "Tel: +254 722307576/ 720717419. \n " +
                            "Email: rodikenya@iconnect.co.ke \n" +
                            "Website: www.rodikenya.org ");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
            Title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intro intro=introList.get(getAdapterPosition());
                    intro.setExpandle(!intro.isExpandle());
                    notifyItemChanged(getAdapterPosition());

                }
            });
            SubTitle=(TextView)itemView.findViewById(R.id.Subtitle);
            description=(Button)itemView.findViewById(R.id.Description);
            description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.rodikenya.org/"));
                    context.startActivity(browserIntent);
                }
            });
            linearLayout=(LinearLayout)itemView.findViewById(R.id.linear_layout);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.expandle_layout);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
    @NonNull
    @Override
    public IntroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.row,parent,false);

        return new IntroViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IntroViewHolder holder, int position) {
         Intro intro=introList.get(position);
         holder.Title.setText(intro.getTitle());
         holder.SubTitle.setText(intro.getDescription());
         //holder.description.setText(intro.getDescription());

         boolean isExpandable=intro.isExpandle();
         holder.relativeLayout.setVisibility(isExpandable?View.VISIBLE:View.GONE);

    }

    @Override
    public int getItemCount() {
        return introList.size();
    }


}
