package fr.kunze.coscanneps;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Julien on 03/10/2016.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Uri uri;

    public ArrayList<String> mItems;
    public int mLayout;
    public static final String NOM_VIDEO_VIDEOCLASSEADAPT="fr.kunze.nomvideorecyclerviewadapter";
    Context mContext;

    public RecyclerViewAdapter(ArrayList<String> objects, int layout, Context context) {
        mItems=objects;
        mContext=context;
        mLayout=layout;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        int count=mItems.size();
        return count;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(mLayout,parent,false);
        ViewHolder vh= new ViewHolder(v,mLayout);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, final int position) {

        String items= mItems.get(position).replace(".mp4"," ");
        holder.mTextView.setText(items.replace("_"," "));
        holder.mVideoView.setBackgroundResource(android.R.drawable.ic_media_play);

        if (position==0) {
            uri = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.enregistrer_une_nouvelle_course);
        }else if(position==1){
            uri=Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.enregistrer_une_nouvelle_course);
        }else if(position==2){
            uri=Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.enregistrer_une_nouvelle_course);
        }else{
         uri=Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.enregistrer_une_nouvelle_course);
        }

            MediaController mc = new MediaController(mContext);
            holder.mVideoView.setMediaController(mc);
            mc.setAnchorView(holder.mVideoView);
            holder.mVideoView.setVideoURI(uri);
            holder.mVideoView.setBackgroundResource(0);
            holder.mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {

                    holder.mVideoView.seekTo(200);
                    holder.mVideoView.requestFocus();

                }


            });

            holder.mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    holder.mVideoView.stopPlayback();
                    holder.mVideoView.setBackgroundResource(android.R.drawable.ic_media_play);
                }
            });


    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;
        public VideoView mVideoView;

        public View rootView;

        public ViewHolder (View itemView,int i){
            super(itemView);
            rootView=itemView;
            mTextView=(TextView)itemView.findViewById(R.id.titreVideo);
            mVideoView=(VideoView) itemView.findViewById(R.id.videoViewCard);

        }
    }

    public void removeItem(int position){

        mItems.remove(position);
        notifyItemRemoved(position);
    }


}
