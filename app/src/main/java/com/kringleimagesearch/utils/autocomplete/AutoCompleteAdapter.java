package com.kringleimagesearch.utils.autocomplete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.kringleimagesearch.R;
import com.kringleimagesearch.data.database.entities.Suggestion;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<Suggestion> {

    private final Context context;
    final private List<Suggestion> items, tempItems, suggestions;

    public AutoCompleteAdapter(Context context, int resource, List<Suggestion> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
        tempItems = new ArrayList<>(items); // this makes the difference.
        suggestions = new ArrayList<>();
    }

    @NotNull
    @Override
    public View getView(int position, final View convertView, @NotNull ViewGroup parent) {

        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                view = inflater.inflate(R.layout.item_autocomplete, parent, false);
            }
        }

        Suggestion nameNCodeModel = items.get(position);
        if (nameNCodeModel != null) {

            TextView txtItem=view.findViewById(R.id.txtItem);
            txtItem.setText(nameNCodeModel.getQry());

        }
        return view;
    }

    @NotNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    private final Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Suggestion) resultValue).getQry();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Suggestion nameNCodeModel : tempItems) {
                    if(nameNCodeModel.getQry().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(nameNCodeModel);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Suggestion> filterList = (ArrayList<Suggestion>) results.values;
            if (results.count > 0) {
                clear();
                for (Suggestion nameNCodeModel: filterList) {
                    add(nameNCodeModel);
                    notifyDataSetChanged();
                    setNotifyOnChange(true);
                }
            }
        }
    };
}