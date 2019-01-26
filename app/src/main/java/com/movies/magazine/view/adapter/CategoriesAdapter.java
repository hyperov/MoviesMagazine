package com.movies.magazine.view.adapter;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.movies.magazine.R;
import com.movies.magazine.data.Category;
import com.movies.magazine.listener.CategoryClickListener;
import com.movies.magazine.databinding.ListItemCategoryBinding;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {
	private List<Category> items;
	private CategoryClickListener customClickListener;
	
	public CategoriesAdapter( List<Category> items ) {
		this.items = items;
	}
	
	@NonNull
	@Override
	public CategoryViewHolder onCreateViewHolder( @NonNull ViewGroup parent,
	                                              int viewType ) {
		ListItemCategoryBinding inflate = DataBindingUtil.inflate( LayoutInflater.from( parent.getContext() )
				, R.layout.list_item_category, parent, false );
		
		return new CategoryViewHolder( inflate );
	}
	
	@Override
	public void onBindViewHolder( @NonNull CategoryViewHolder holder, int position ) {
		Category item = items.get( position );
		holder.set( item );
	}
	
	@Override
	public int getItemCount( ) {
		if ( items == null ) {
			return 0;
		}
		return items.size();
	}
	
	public class CategoryViewHolder extends RecyclerView.ViewHolder {
		
		private ListItemCategoryBinding listItemCategoryBinding;
		
		public CategoryViewHolder( ListItemCategoryBinding dataBinding ) {
			super( dataBinding.getRoot() );
			this.listItemCategoryBinding = dataBinding;
		}
		
		public void set( Category item ) {
			//UI setting code
			
			listItemCategoryBinding.setCategory( item );
			listItemCategoryBinding.setClick( customClickListener );
			listItemCategoryBinding.executePendingBindings();
		}
	}
	
	public void setCustomClickListener( CategoryClickListener customClickListener ) {
		this.customClickListener = customClickListener;
	}
	
	@BindingAdapter({ "imageResource_binding" })
	public static void setImageViewResource( ImageView imageView, @DrawableRes int resource ) {
		imageView.setImageResource( resource );
	}
}