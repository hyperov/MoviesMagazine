package com.movies.magazine.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.movies.magazine.R;
import com.movies.magazine.data.Movie;
import com.movies.magazine.viewmodel.MovieDetailsViewModel;
import com.movies.magazine.viewmodel.ViewModelFactory;
import com.movies.magazine.databinding.ActivityMovieDetailsBinding;

import javax.inject.Inject;

public class MovieDetailsActivity extends AppCompatActivity {
	
	@Inject
	ViewModelFactory viewModelFactory;
	private MovieDetailsViewModel movieDetailsViewModel;
	private ActivityMovieDetailsBinding movieDetailsBinding;
	private ShareActionProvider mShareActionProvider;
	private String shareBodyText;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		movieDetailsBinding =
				DataBindingUtil.setContentView( this, R.layout.activity_movie_details );
		
		initAppBar();
		initFab();
		createAndObserveViewModel();
		fillViewModel();
		
	}
	
	private void createAndObserveViewModel( ) {
		movieDetailsViewModel = ViewModelProviders.of( this, viewModelFactory ).get( MovieDetailsViewModel.class );
		movieDetailsViewModel.getMovie().observe( this, new Observer<Movie>() {
			@Override
			public void onChanged( @Nullable Movie movie ) {
				movieDetailsBinding.setMovieViewModel( movieDetailsViewModel );
				shareBodyText = "         " + movie.getMovieName() + "     \n" +
						"\nIngredients:\n" +
						"============\n" + movie.getIngredients() + "\n"
						+ "==========================================\n"
						+ "Method:\n" +
						"========\n" + movie.getTrivia() + "\n";
			}
		} );
		
	}
	
	private void fillViewModel( ) {
		Movie movie = getIntent().getParcelableExtra( "rec" );
		movieDetailsViewModel.getMovie().setValue( movie );
	}
	
	private void initFab( ) {
		movieDetailsBinding.fab.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				Snackbar.make( view, "Replace with your own action", Snackbar.LENGTH_LONG )
						.setAction( "Action", null ).show();
			}
		} );
	}
	
	private void initAppBar( ) {
		setSupportActionBar( movieDetailsBinding.toolbar );
		getSupportActionBar().setDisplayHomeAsUpEnabled( true );
		
	}
	
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		getMenuInflater().inflate( R.menu.menu_movie_details, menu );
		MenuItem item = menu.findItem( R.id.action_share );
		mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider( item );
		Intent intent = getShareIntent();
		setShareIntent( intent );
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		if ( item.getItemId() == R.id.action_share ) {
			Intent intent = getShareIntent();
			setShareIntent( intent );
			startActivity( Intent.createChooser( intent, "Choose sharing trivia" ) );
			return true;
		}
		return super.onOptionsItemSelected( item );
	}
	
	@NonNull
	private Intent getShareIntent( ) {
		Intent intent = new Intent( Intent.ACTION_SEND );
		intent.setType( "text/plain" );
		intent.putExtra( Intent.EXTRA_TEXT, shareBodyText );
		return intent;
	}
	
	private void setShareIntent( Intent shareIntent ) {
		if ( mShareActionProvider != null ) {
			mShareActionProvider.setShareIntent( shareIntent );
		}
	}
}
