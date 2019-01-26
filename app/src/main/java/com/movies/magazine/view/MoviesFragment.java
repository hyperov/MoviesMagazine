package com.movies.magazine.view;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.movies.magazine.R;
import com.movies.magazine.data.Movie;
import com.movies.magazine.listener.MovieFavClickListener;
import com.movies.magazine.listener.MovieItemClickListener;
import com.movies.magazine.view.adapter.MoviesAdapter;
import com.movies.magazine.view.itemdecoration.ItemOffsetDecoration;
import com.movies.magazine.viewmodel.MainViewModel;
import com.movies.magazine.viewmodel.MoviesViewModel;
import com.movies.magazine.viewmodel.ViewModelFactory;
import com.movies.magazine.databinding.FragmentMoviesBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class MoviesFragment extends DaggerFragment
		implements MovieFavClickListener, MovieItemClickListener {
	
	private static final String ARG_CATEGORY_NAME = "categoryName";
	private static final String ARG_FAV_MODE = "favMode";
	public static final String ARG_FAV_MODE_YES = "YES";
	public static final String ARG_FAV_MODE_NO = "NO";
	
	private String categoryName;
	private String favMode;
	
	
	@Inject
	ViewModelFactory viewModelFactory;
	
	private MoviesViewModel moviesViewModel;
	private RecyclerView recyclerView;
	private MainViewModel mainViewModel;
	private MoviesAdapter moviesAdapter;
	private Observer<List<Movie>> movieListObserver;
	private FragmentMoviesBinding fragmentMoviesBinding;
	
	public MoviesFragment( ) {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param categoryName Parameter 1.
	 * @param favMode      Parameter 2.
	 * @return A new instance of fragment MoviesFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static MoviesFragment newInstance( String categoryName, String favMode ) {
		MoviesFragment fragment = new MoviesFragment();
		Bundle args = new Bundle();
		args.putString( ARG_CATEGORY_NAME, categoryName );
		args.putString( ARG_FAV_MODE, favMode );
		fragment.setArguments( args );
		return fragment;
	}
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		if ( getArguments() != null ) {
			categoryName = getArguments().getString( ARG_CATEGORY_NAME );
			favMode = getArguments().getString( ARG_FAV_MODE );
		}
	}
	
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState ) {
		// Inflate the layout for this fragment
		fragmentMoviesBinding = DataBindingUtil.inflate( inflater,
				R.layout.fragment_movies, container, false );
		recyclerView = fragmentMoviesBinding.idRecyclerMovies;
		ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration( getContext(), R.dimen.item_offset );
		recyclerView.addItemDecoration( itemDecoration );
		return fragmentMoviesBinding.getRoot();
	}
	
	@Override
	public void onViewCreated( @NonNull final View view, @Nullable Bundle savedInstanceState ) {
		super.onViewCreated( view, savedInstanceState );
		moviesViewModel = ViewModelProviders.of( this, viewModelFactory ).get( MoviesViewModel.class );
		mainViewModel = ViewModelProviders.of( getActivity(), viewModelFactory ).get( MainViewModel.class );
		getMovies();
		moviesViewModel.getMovies( categoryName );
		observeUpdateMovie( view );
		
	}
	
	private void getMovies( ) {
		movieListObserver = new Observer<List<Movie>>() {
			@Override
			public void onChanged( @Nullable List<Movie> movies ) {
				setMoviesData( movies );
				if ( ARG_FAV_MODE_YES.equalsIgnoreCase( favMode ) ) {
					moviesViewModel.getFavCount().setValue( movies.size() );
				}
			}
		};
		if ( ARG_FAV_MODE_YES.equalsIgnoreCase( favMode ) ) {
			
			fragmentMoviesBinding.setIsFavEmpty( true );
			fragmentMoviesBinding.setIsFavScreen( true );
			
			observeFavCount();
			moviesViewModel.getFavMovies().observe( this, movieListObserver );
		} else {
			moviesViewModel.getAllMovies().observe( this, movieListObserver );
			fragmentMoviesBinding.setIsFavEmpty( false );
			fragmentMoviesBinding.setIsFavScreen( false );
			
		}
		
	}
	
	private void observeFavCount( ) {
		moviesViewModel.getFavCount().observe( this, new Observer<Integer>() {
			@Override
			public void onChanged( @Nullable Integer integer ) {
				int favCount = integer.intValue();
				if ( favCount == 0 )
					fragmentMoviesBinding.setIsFavEmpty( true );
				else
					fragmentMoviesBinding.setIsFavEmpty( false );
			}
		} );
	}
	
	private void observeUpdateMovie( @NonNull final View view ) {
		moviesViewModel.getFavMovieName().observe( this, new Observer<String>() {
			@Override
			public void onChanged( @Nullable String movieName ) {
				Snackbar.make( view.findViewById( R.id.framelayout ), "update fav: " +
						movieName, Snackbar.LENGTH_LONG ).show();
			}
		} );
	}
	
	private void setMoviesData( @Nullable List<Movie> movies ) {
		moviesAdapter = new MoviesAdapter( movies, favMode, this );
		moviesAdapter.setMovieItemClickListener( this );
		recyclerView.setAdapter( moviesAdapter );
	}
	
	@Override
	public void onMovieFav( boolean isFav, @NotNull Movie item ) {
		moviesViewModel.updateMovie( item, isFav );
	}
	
	@Override
	public void onMovieItemClick( @NotNull Movie movieItem ) {
		mainViewModel.getClickedMovie().setValue( movieItem );
	}
}