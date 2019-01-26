package com.movies.magazine.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.movies.magazine.R;
import com.movies.magazine.data.Movie;

import java.util.List;

public class SearchableAdapter extends RecyclerView.Adapter<SearchableAdapter.MovieViewHolder> {
	private final Context context;
	
	private List<Movie> items;
	
	public SearchableAdapter( List<Movie> items, Context context ) {
		this.items = items;
		this.context = context;
	}
	
	public List<Movie> getItems( ) {
		return items;
	}
	
	@Override
	public MovieViewHolder onCreateViewHolder( ViewGroup parent,
	                                            int viewType ) {
		View v = LayoutInflater.from( parent.getContext() )
				.inflate( R.layout.list_item_search, parent, false );
		return new MovieViewHolder( v );
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
	
	
	class MovieViewHolder extends RecyclerView.ViewHolder {
		
		TextView searchMovie;
		
		MovieViewHolder( View itemView ) {
			super( itemView );
			searchMovie = itemView.findViewById( R.id.search_movie );
		}
		
		void set( Movie item ) {
			//UI setting code
			searchMovie.setText( item.getMovieName() );
		}
	}
}