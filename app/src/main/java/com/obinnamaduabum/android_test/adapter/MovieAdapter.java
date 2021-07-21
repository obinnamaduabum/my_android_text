package com.obinnamaduabum.android_test.adapter;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.obinnamaduabum.android_test.R;
import com.obinnamaduabum.android_test.model.MovieModel;
import com.obinnamaduabum.android_test.util.GetBitmapTask;
import com.obinnamaduabum.android_test.util.ImageUtil;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter  extends RecyclerView.Adapter<MovieAdapter.MovieVH> {

    public List<MovieModel> movieList;
    private static final String TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(List<MovieModel> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        return new MovieVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MovieVH holder, int position) {
        MovieModel movieModel = movieList.get(position);
        holder.mMovieTitle.setText(movieModel.getTitle());
        holder.mMovieRating.setText(movieModel.getImdbRating());
        boolean isExpanded = movieModel.isExpanded();
        holder.mMovieYear.setText(movieModel.getYear());
        holder.mMovieGenre.setText(movieModel.getGenre());

        if(movieModel.getPoster() != null) {
//            new GetBitmapTask(holder).execute(movieModel.getPoster());
            String imageUrl = movieModel.getPoster();
            Picasso.get().load(imageUrl).into(holder.mMovieImage);
            // ImageUtil.setImage(holder.mMovieImage.getContext(), movieModel.getPoster(), holder.mMovieImage);
        }

        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieVH extends RecyclerView.ViewHolder {

        TextView mMovieTitle;
        TextView mMovieRating;
        ImageView mMovieImage;
        RelativeLayout expandableLayout;
        RelativeLayout mMovieHeader;
        Button userGroupDefault;
        ProgressBar progressBarLoading;
        TextView mMovieGenre;
        TextView mMovieYear;

        public MovieVH(@NonNull View itemView) {
            super(itemView);
            mMovieTitle = itemView.findViewById(R.id.movie_title);
            mMovieImage = itemView.findViewById(R.id.movie_image);
            mMovieRating = itemView.findViewById(R.id.movie_rating);
            expandableLayout = itemView.findViewById(R.id.movie_header_expanded);
            mMovieHeader = itemView.findViewById(R.id.movie_header);

            mMovieGenre = itemView.findViewById(R.id.movie_genre);
            mMovieYear = itemView.findViewById(R.id.movie_year);

            mMovieHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    movieList.get(position).setExpanded(!movieList.get(position).isExpanded());
                    notifyDataSetChanged();
                }
            });
        }



    }

    public void addedNewData(MovieModel movieModel){
        movieList.add(movieModel);
        // notifyDataSetChanged();
    }
}