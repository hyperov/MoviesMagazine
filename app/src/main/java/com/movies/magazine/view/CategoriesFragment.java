package com.movies.magazine.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.movies.magazine.R;
import com.movies.magazine.data.Category;
import com.movies.magazine.listener.CategoryClickListener;
import com.movies.magazine.view.adapter.CategoriesAdapter;
import com.movies.magazine.viewmodel.CategoriesViewModel;
import com.movies.magazine.viewmodel.MainViewModel;
import com.movies.magazine.viewmodel.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class CategoriesFragment extends DaggerFragment implements CategoryClickListener {
	
	@Inject
	ViewModelFactory viewModelFactory;
	private RecyclerView recyclerView;
	private CategoriesAdapter categoriesAdapter;
	private MainViewModel mainViewModel;
	
	public static CategoriesFragment newInstance( ) {
		return new CategoriesFragment();
	}
	
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                          @Nullable Bundle savedInstanceState ) {
		
		View inflate = inflater.inflate( R.layout.fragment_categories, container, false );
		recyclerView = inflate.findViewById( R.id.id_recycler_categories );
		return inflate;
	}
	
	@Override
	public void onActivityCreated( @Nullable Bundle savedInstanceState ) {
		super.onActivityCreated( savedInstanceState );
		
		CategoriesViewModel mViewModel = ViewModelProviders.of( this, viewModelFactory ).get( CategoriesViewModel.class );
		mainViewModel = ViewModelProviders.of( getActivity(), viewModelFactory ).get( MainViewModel.class );
		
		mViewModel.getCategoriesList().observe( this, new Observer<List<Category>>() {
			@Override
			public void onChanged( @Nullable List<Category> categories ) {
				categoriesAdapter = new CategoriesAdapter( categories );
				categoriesAdapter.setCustomClickListener( CategoriesFragment.this );
				recyclerView.setAdapter( categoriesAdapter );
			}
		} );
		
		mViewModel.getCategories();
	}
	
	@Override
	public void onCategoryClick( @NotNull String categoryName ) {
		mainViewModel.getClickedCategory().setValue( categoryName );
	}
}
