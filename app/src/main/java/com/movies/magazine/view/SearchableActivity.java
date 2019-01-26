package com.movies.magazine.view;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.movies.magazine.R;
import com.movies.magazine.data.Movie;
import com.movies.magazine.listener.MovieFavClickListener;
import com.movies.magazine.listener.MovieItemClickListener;
import com.movies.magazine.view.adapter.MoviesAdapter;
import com.movies.magazine.view.adapter.SearchableAdapter;
import com.movies.magazine.view.itemdecoration.ItemOffsetDecoration;
import com.movies.magazine.viewmodel.SearchViewModel;
import com.movies.magazine.viewmodel.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class SearchableActivity extends DaggerAppCompatActivity implements MovieItemClickListener, MovieFavClickListener {
	
	@Inject
	ViewModelFactory viewModelFactory;
	private SearchViewModel searchViewModel;
	private SearchableAdapter searchableAdapter;
	private MoviesAdapter moviesAdapter;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_searchable );
		initViews();
		searchViewModel = ViewModelProviders.of( this, viewModelFactory ).get( SearchViewModel.class );
		observeSearchList();
		observeError();
		observeFavClicked();
		handleIntent( getIntent() );
	}
	
	private void initViews( ) {
		RecyclerView recyclerView = findViewById( R.id.search_list );
		recyclerView.addItemDecoration( new ItemOffsetDecoration( this, R.dimen.item_offset ) );

//		searchableAdapter = new SearchableAdapter( new ArrayList<Movie>(), this );
//		recyclerView.setAdapter( searchableAdapter );
		
		moviesAdapter = new MoviesAdapter( new ArrayList<Movie>(), MoviesFragment.ARG_FAV_MODE_NO, this );
		moviesAdapter.setMovieItemClickListener( this );
		recyclerView.setAdapter( moviesAdapter );
	}
	
	@Override
	protected void onNewIntent( Intent intent ) {
		super.onNewIntent( intent );
		handleIntent( intent );
	}
	
	private void handleIntent( Intent intent ) {
		
		if ( Intent.ACTION_SEARCH.equals( intent.getAction() ) )
		
		{
			String query = intent.getStringExtra( SearchManager.QUERY );
			doMySearch( query );
		}
	}
	
	private void doMySearch( String query ) {
		searchViewModel.getSearchResults( query );
	}
	
	private void observeSearchList( ) {
		searchViewModel.getSearchList().observe( this, new Observer<List<Movie>>() {
			@Override
			public void onChanged( @Nullable List<Movie> movies ) {
//				searchableAdapter.getItems().clear();
//				searchableAdapter.getItems().addAll( movies );
//				searchableAdapter.notifyDataSetChanged();
				
				moviesAdapter.getItems().clear();
				moviesAdapter.getItems().addAll( movies );
				moviesAdapter.notifyDataSetChanged();
			}
		} );
	}
	
	private void observeFavClicked( ) {
		searchViewModel.getFavMovieName().observe( this, new Observer<String>() {
			@Override
			public void onChanged( @Nullable String movieName ) {
				Snackbar.make( findViewById( R.id.main_search_layout ), "update fav: " +
						movieName, Snackbar.LENGTH_LONG ).show();
			}
		} );
	}
	
	private void observeError( ) {
		searchViewModel.getErrorHolder().observe( this, new Observer<Throwable>() {
			@Override
			public void onChanged( @Nullable Throwable throwable ) {
				Toast.makeText( getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT ).show();
			}
		} );
	}
	
	@Override
	public void onMovieItemClick( @NotNull Movie movieItem ) {
		Intent intent = new Intent( this, MovieDetailsActivity.class );
		intent.putExtra( "rec", movieItem );
		startActivity( intent );
	}
	
	@Override
	public void onMovieFav( boolean isFav, @NotNull Movie item ) {
		searchViewModel.updateMovie( item,isFav );
	}
}
