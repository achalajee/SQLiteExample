package com.codefoundrysl.ar.sqliteexample.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.codefoundrysl.ar.sqliteexample.R;
import com.codefoundrysl.ar.sqliteexample.adapter.ProductListAdapter;
import com.codefoundrysl.ar.sqliteexample.database.DatabaseHandler;
import com.codefoundrysl.ar.sqliteexample.helpers.RecyclerItemTouchHelper;
import com.codefoundrysl.ar.sqliteexample.holders.ProductViewHolder;
import com.codefoundrysl.ar.sqliteexample.models.Product;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener  {
    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductListAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;

    private EditText eTxtName, eTxtDescription, eTxtPrice, eTxtThumb;

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHandler(this);

        eTxtName = findViewById(R.id.eTxtName);
        eTxtDescription = findViewById(R.id.eTxtDescription);
        eTxtPrice = findViewById(R.id.eTxtPrice);
        eTxtThumb = findViewById(R.id.eTxtThumb);
        recyclerView = findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        productList = new ArrayList<>();
        getData();

        mAdapter = new ProductListAdapter(this, productList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }

    public void onClickSave(View v) {
        String name = eTxtName.getText().toString();
        String des = eTxtDescription.getText().toString();
        String price = eTxtPrice.getText().toString();
        String thumb = eTxtThumb.getText().toString();
        Product product = new Product(name, des, price, thumb);

        db.addProduct(product);

        getData();
        mAdapter.notifyDataSetChanged();
    }

    private void getData() {
        productList = db.getAllContacts();

        for (Product cn : productList) {
            String log = "Id: " + cn.getId() + " ,Name: " + cn.getName() + " ,Description: " +
                    cn.getDescription();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ProductViewHolder) {
            // get the removed item name to display it in snack bar
            String name = productList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final Product deletedItem = productList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
