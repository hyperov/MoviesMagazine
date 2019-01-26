package com.movies.magazine.view.adapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.movies.magazine.data.Movie;
import com.movies.magazine.databinding.ListItemMovieBinding;
import com.movies.magazine.listener.MovieFavClickListener;
import com.movies.magazine.listener.MovieItemClickListener;
import com.movies.magazine.view.MoviesFragment;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
	
	private List<Movie> items;
	private String isFavMode;
	private MovieFavClickListener movieClickListener;
	private MovieItemClickListener movieItemClickListener;
	
	public MoviesAdapter( List<Movie> items, String isFavMode, MovieFavClickListener movieClickListener ) {
		this.items = items;
		this.isFavMode = isFavMode;
		this.movieClickListener = movieClickListener;
	}
	
	public List<Movie> getItems( ) {
		return items;
	}
	
	@Override
	public MovieViewHolder onCreateViewHolder( ViewGroup parent,
	                                           int viewType ) {
		LayoutInflater layoutInflater = LayoutInflater.from( parent.getContext() );
		
		ListItemMovieBinding movieBinding = ListItemMovieBinding.inflate( layoutInflater );
		return new MovieViewHolder( movieBinding );
	}
	
	@Override
	public void onBindViewHolder( MovieViewHolder holder, int position ) {
		Movie item = items.get( position );
		holder.set( item );
	}
	
	@Override
	public int getItemCount( ) {
		if ( items == null ) {
			return 0;
		}
		return items.size();
	}
	
	public void setMovieItemClickListener( MovieItemClickListener movieItemClickListener ) {
		this.movieItemClickListener = movieItemClickListener;
	}
	
	class MovieViewHolder extends RecyclerView.ViewHolder {
		
		private final ListItemMovieBinding movieBinding;
		
		MovieViewHolder( final ListItemMovieBinding movieBinding ) {
			super( movieBinding.getRoot() );
			this.movieBinding = movieBinding;
		}
		
		void set( final Movie item ) {
			//UI setting code
			movieBinding.setMovieItem( item );
			movieBinding.setClick( movieItemClickListener );
			if ( isFavMode.equalsIgnoreCase( MoviesFragment.ARG_FAV_MODE_YES ) )
				movieBinding.setIsFavScreen( true );
			else {
				movieBinding.setIsFavScreen( false );
			}
			movieBinding.btFav.setOnClickListener( new View.OnClickListener() {
				@Override
				public void onClick( View v ) {
					movieBinding.btFav.isFavorite();
					boolean toggleFav = !movieBinding.getMovieItem().isFav();
					movieBinding.btFav.setFavoriteAnimated( toggleFav );
					movieBinding.getMovieItem().setFav( toggleFav );
					movieClickListener.onMovieFav( toggleFav, movieBinding.getMovieItem() );
				}
			} );
			
		}
	}
	
	@BindingAdapter("toggle_favourite")
	public static void setFavourite( MaterialFavoriteButton materialFavoriteButton, boolean isFav ) {
		if ( materialFavoriteButton.isFavorite() != isFav )
			materialFavoriteButton.setFavorite( isFav );
	}
	
	
	
}