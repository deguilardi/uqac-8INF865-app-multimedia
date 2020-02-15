package ca.uqac.mysongs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<SongEntity> mSongs;

    final private OnClickHandler mClickHandler;

    public interface OnClickHandler {
        void onClick(SongEntity song, SongsAdapter.ViewHolder adapterViewHolder);
    }

    SongsAdapter(Context context, ArrayList<SongEntity> songs, OnClickHandler clickHandler){
        mContext = context;
        mSongs = songs;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_song, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.setTitle( mSongs.get( position ).getName() );
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
        }

        void setTitle( String title ){
            mTitle.setText( title );
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onClick(mSongs.get( position ) , this);
        }
    }
}
