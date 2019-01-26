package com.movies.magazine.view;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.kobakei.ratethisapp.RateThisApp;
import com.movies.magazine.R;
import com.movies.magazine.data.Movie;
import com.movies.magazine.viewmodel.MainViewModel;
import com.movies.magazine.viewmodel.MovieDetailsViewModel;
import com.movies.magazine.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.cketti.mailto.EmailIntentBuilder;

import static com.movies.magazine.extensions.FragmentExtensionsKt.CATEGORY_ADVENTURES;
import static com.movies.magazine.extensions.FragmentExtensionsKt.CATEGORY_ANIMATION;
import static com.movies.magazine.extensions.FragmentExtensionsKt.CATEGORY_COMEDY;
import static com.movies.magazine.extensions.FragmentExtensionsKt.CATEGORY_SCI_FI;
import static com.movies.magazine.extensions.FragmentExtensionsKt.replaceFragment;

public class MainActivity extends DaggerAppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	
	private DrawerLayout drawer;
	private SearchView searchView;
	
	
	@Inject
	ViewModelFactory viewModelFactory;
	private MovieDetailsViewModel movieDetailsViewModel;
	private MainViewModel mainViewModel;
	
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		initViews();
		prepareViewModel();
		insertMockData();
	}
	
	private void initViews( ) {
		Toolbar toolbar = findViewById( R.id.toolbar );
		setSupportActionBar( toolbar );
		
		initDrawer( toolbar );
		
		NavigationView navigationView = findViewById( R.id.nav_view );
		navigationView.setNavigationItemSelectedListener( this );
		
	}
	
	@Override
	public void onBackPressed( ) {
		if ( drawer.isDrawerOpen( GravityCompat.START ) ) {
			drawer.closeDrawer( GravityCompat.START );
		} else if ( getSupportFragmentManager().getBackStackEntryCount() > 0 ) {
			getSupportFragmentManager().popBackStack();
		} else {
			RateThisApp.setCallback( new RateThisApp.Callback() {
				@Override
				public void onYesClicked( ) {
				
				}
				
				@Override
				public void onNoClicked( ) {
					finish();
				}
				
				@Override
				public void onCancelClicked( ) {
				}
			} );
			RateThisApp.showRateDialog( this );
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		getMenuInflater().inflate( R.menu.main, menu );
		searchView = (SearchView) menu.findItem( R.id.action_search )
				.getActionView();
		
		searchView.setSubmitButtonEnabled( true );
		SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE );
		ComponentName componentName = new ComponentName( this, SearchableActivity.class );
		searchView.setSearchableInfo( searchManager.getSearchableInfo( componentName ) );
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		int id = item.getItemId();
		
		if ( id == R.id.action_search ) {
			onSearchRequested();
			return true;
		}
		
		return super.onOptionsItemSelected( item );
	}
	
	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected( @NonNull MenuItem item ) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		
		if ( id == R.id.nav_home ) {
			replaceFragment( this,
					CategoriesFragment.newInstance(),
					R.id.layout_id,
					false );
		} else if ( id == R.id.nav_favourites ) {
			getSupportFragmentManager().popBackStack();
			replaceFragment( this,
					MoviesFragment.newInstance( "", MoviesFragment.ARG_FAV_MODE_YES ),
					R.id.layout_id,
					false );
		} else if ( id == R.id.nav_rate_app ) {
			showDialog();
		} else if ( id == R.id.nav_send_feedback ) {
			EmailIntentBuilder.from( this )
					.to( "support@example.org" )
					.subject( "Feedback" )
					.body( "Enter Message here" )
					.start();
			
		} else if ( id == R.id.nav_share ) {
		
		}
		
		drawer.closeDrawer( GravityCompat.START );
		return true;
	}
	
	
	private void initDrawer( Toolbar toolbar ) {
		drawer = findViewById( R.id.drawer_layout );
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
		drawer.addDrawerListener( toggle );
		toggle.syncState();
	}
	
	private void prepareViewModel( ) {
		initViewModels();
		observeFirstInsert();
		observeCategoryClick();
		observeMovieClick();
	}
	
	
	private void initViewModels( ) {
		movieDetailsViewModel = ViewModelProviders.of( this, viewModelFactory ).get( MovieDetailsViewModel.class );
		mainViewModel = ViewModelProviders.of( this, viewModelFactory ).get( MainViewModel.class );
	}
	
	private void observeFirstInsert( ) {
		mainViewModel.isInserted().observe( this, new Observer<Long>() {
			@Override
			public void onChanged( Long isInserted ) {
				Snackbar.make( findViewById( R.id.coordinator ), "insert success : " +
						isInserted.toString(), Snackbar.LENGTH_LONG ).show();
				replaceFragment( MainActivity.this,
						CategoriesFragment.newInstance(),
						R.id.layout_id,
						false );
			}
		} );
	}
	
	private void observeMovieClick( ) {
		mainViewModel.getClickedMovie().observe( this, new Observer<Movie>() {
			@Override
			public void onChanged( @Nullable Movie movie ) {
				Intent intent = new Intent( MainActivity.this, MovieDetailsActivity.class );
				intent.putExtra( "rec", movie );
				startActivity( intent );
			}
		} );
	}
	
	private void observeCategoryClick( ) {
		mainViewModel.getClickedCategory().observe( this, new Observer<String>() {
			@Override
			public void onChanged( @Nullable String categoryName ) {
				replaceFragment( MainActivity.this,
						MoviesFragment.newInstance( categoryName, MoviesFragment.ARG_FAV_MODE_NO ),
						R.id.layout_id,
						true );
			}
		} );
	}
	
	private void insertMockData( ) {
		String[] movies = getResources().getStringArray( R.array.movies );
		String[] storyLines = getResources().getStringArray( R.array.story_lines );
		String[] methods = getResources().getStringArray( R.array.methods );
		String[] durationTime = getResources().getStringArray( R.array.duration_time );
		
		//adventures movies
		Movie movie1 = new Movie( null, movies[0], getString( R.string.desc ),
				storyLines[0], methods[0], R.drawable.aquaman, CATEGORY_ADVENTURES, false, durationTime[0] );
		Movie movie2 = new Movie( null, movies[1], getString( R.string.desc ),
				storyLines[1], "", R.drawable.game_of_thrones, CATEGORY_ADVENTURES, false, durationTime[1] );
		Movie movie3 = new Movie( null, movies[2], getString( R.string.desc ),
				storyLines[2], "", R.drawable.spiderman_into_the_spider_verse, CATEGORY_ADVENTURES, false, durationTime[2] );
		Movie movie4 = new Movie( null, movies[3], getString( R.string.desc ),
				storyLines[3], "", R.drawable.avengers_endgame, CATEGORY_ADVENTURES, false, durationTime[3] );
		Movie movie5 = new Movie( null, movies[4], getString( R.string.desc ),
				storyLines[4], "", R.drawable.harrypotter, CATEGORY_ADVENTURES, false, durationTime[4] );
		
		Movie movie6 = new Movie( null, movies[5], getString( R.string.desc ),
				storyLines[5], "", R.drawable.guardians_of_the_galaxy, CATEGORY_COMEDY, false, durationTime[5] );
		Movie movie7 = new Movie( null, movies[6], getString( R.string.desc ),
				storyLines[6], "", R.drawable.jumanji, CATEGORY_COMEDY, false, durationTime[6] );
		Movie movie8 = new Movie( null, movies[7], getString( R.string.desc ),
				storyLines[7], "", R.drawable.series_of_unfortinate_events, CATEGORY_COMEDY, false, durationTime[7] );
		Movie movie9 = new Movie( null, movies[8], getString( R.string.desc ),
				storyLines[8], "", R.drawable.lego, CATEGORY_COMEDY, false, durationTime[8] );
		Movie movie10 = new Movie( null, movies[9], getString( R.string.desc ),
				storyLines[9], "", R.drawable.deadpool, CATEGORY_COMEDY, false, durationTime[9] );
		
		Movie movie11 = new Movie( null, movies[10], getString( R.string.desc ),
				storyLines[10], "", R.drawable.back_to_the_future_the_game, CATEGORY_SCI_FI, false, durationTime[10] );
		Movie movie12 = new Movie( null, movies[11], getString( R.string.desc ),
				storyLines[11], "", R.drawable.doctor_who, CATEGORY_SCI_FI, false, durationTime[11] );
		Movie movie13 = new Movie( null, movies[12], getString( R.string.desc ),
				storyLines[12], "", R.drawable.twelve_monkeys, CATEGORY_SCI_FI, false, durationTime[12] );
		Movie movie14 = new Movie( null, movies[13], getString( R.string.desc ),
				storyLines[13], "", R.drawable.x_files, CATEGORY_SCI_FI, false, durationTime[12] );
		Movie movie15 = new Movie( null, movies[14], getString( R.string.desc ),
				storyLines[14], "", R.drawable.fringe, CATEGORY_SCI_FI, false, durationTime[12] );
		
		Movie movie16 = new Movie( null, movies[15], getString( R.string.desc ),
				storyLines[15], "", R.drawable.incredibles, CATEGORY_ANIMATION, false, durationTime[13] );
		Movie movie17 = new Movie( null, movies[16], getString( R.string.desc ),
				storyLines[16], "", R.drawable.rick_and_morty, CATEGORY_ANIMATION, false, durationTime[14] );
		Movie movie18 = new Movie( null, movies[17], getString( R.string.desc ),
				storyLines[17], "", R.drawable.big_hero, CATEGORY_ANIMATION, false, durationTime[14] );
		Movie movie19 = new Movie( null, movies[18], getString( R.string.desc ),
				storyLines[18], "", R.drawable.frozen, CATEGORY_ANIMATION, false, durationTime[14] );
		
		mainViewModel.insertMovies(
				movie1, movie2, movie3, movie4,
				movie5, movie6, movie7, movie8, movie9,
				movie10, movie11, movie12, movie13, movie14, movie15,
				movie16, movie17, movie18, movie19 );
	}
	
	private void showDialog( ) {
		String appPackage = getPackageName();
		String url = "market://details?id=" + appPackage;
		try {
			startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse( url ) ) );
		} catch (android.content.ActivityNotFoundException anfe) {
			startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse( "http://play.google.com/store/apps/details?id=" + getPackageName() ) ) );
		}
	}
	
	
}
