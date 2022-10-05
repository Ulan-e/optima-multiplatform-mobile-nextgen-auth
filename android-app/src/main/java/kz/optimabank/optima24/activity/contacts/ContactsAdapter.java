package kz.optimabank.optima24.activity.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kz.optimabank.optima24.activity.contacts.data.Contact;
import kz.optimabank.optima24.databinding.ItemContactBinding;

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final Context context;

    private List<Contact> contactList;
    private List<Contact> contactListFiltered;

    private OnItemClickListener clickListener;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Contact> data) {
        this.contactList = data;
        this.contactListFiltered = contactList;
        notifyDataSetChanged();
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ContactsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemContactBinding binding = ItemContactBinding.inflate(inflater, parent, false);
        return new ContactViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Contact contact = contactListFiltered.get(position);
        ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
        contactViewHolder.bind(contact);
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private final Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String searchPattern = constraint.toString().toLowerCase(Locale.ROOT).trim();
            List<Contact> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                contactListFiltered = contactList;
            } else {
                for (Contact item : contactList) {
                    if (item.getContactName().toLowerCase(Locale.ROOT).contains(searchPattern)
                            || item.getPhoneNumber().toLowerCase(Locale.ROOT).contains(searchPattern)) {
                        filteredList.add(item);
                    }
                }
                contactListFiltered = filteredList;
            }
            FilterResults results = new FilterResults();
            results.values = contactListFiltered;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contactListFiltered = (List<Contact>) results.values;
            notifyDataSetChanged();
        }
    };

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        private final ItemContactBinding binding;

        public ContactViewHolder(@NonNull ItemContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(Contact contact) {
            binding.getRoot().setOnClickListener(v -> clickListener.onItemClicked(contact));
            binding.contactName.setText(contact.getContactName());
            binding.contactNumber.setText(contact.getPhoneNumber());
            binding.avatarViewContact.setTextInitials(contact.getContactName());
        }
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    interface OnItemClickListener {
        void onItemClicked(Contact contact);
    }
}