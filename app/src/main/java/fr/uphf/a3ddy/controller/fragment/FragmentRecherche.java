package fr.uphf.a3ddy.controller.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;

import fr.uphf.a3ddy.R;
import fr.uphf.a3ddy.model.UtilisateurSecurity;
import fr.uphf.a3ddy.service.AppService;
import fr.uphf.a3ddy.service.LoadFragmentService;


public class FragmentRecherche extends Fragment {
    View view;
    Context context;
    private AppService appService;
    private int currentPage = 0;
    private boolean isLoading = false;
    private UtilisateurSecurity userS;
    AutoCompleteTextView autoCompleteTextView;
    private LoadFragmentService loadFragmentService;
    RecyclerView resultatsRecherche;

    public void iniUI() {
        autoCompleteTextView = view.findViewById(R.id.autoComplete);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setAdapter(adapter);
        resultatsRecherche = view.findViewById(R.id.resultats_recherche);
    }

    public void setListeners() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_recherche, container, false);;
        context = getContext();
        appService = (AppService) getActivity().getApplication();
        userS = appService.getUtilisateurSecurity();
        loadFragmentService = new LoadFragmentService(this);
        return view;
    }
}