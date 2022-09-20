package com.example.mensaapi.data_fetcher.dataclasses;

import com.example.mensaapi.data_fetcher.dataclasses.enums.FoodProviderType;
import com.example.mensaapi.data_fetcher.dataclasses.interfaces.FetchedFoodProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FetchedData {
    List<FetchedFoodProvider> fetchedFoodProviders;
    public FetchedData() {
        this.fetchedFoodProviders = new ArrayList<>();
    }
    public FetchedData(List<FetchedFoodProvider> fetchedFoodProviders) {
        this.fetchedFoodProviders = fetchedFoodProviders;
    }

    public List<FetchedCanteen> getFetchedCanteens() {
        return fetchedFoodProviders.stream()
                .filter(fetchedFoodProvider -> fetchedFoodProvider.getType() == FoodProviderType.CANTEEN)
                .map(FetchedCanteen.class::cast)
                .collect(Collectors.toList());
    }

    public void addFetchedCanteen(FetchedFoodProvider fetchedCanteen) {
        this.fetchedFoodProviders.add(fetchedCanteen);
    }

    public List<FetchedCafeteria> getFetchedCafeterias() {
        return fetchedFoodProviders.stream()
                .filter(fetchedFoodProvider -> fetchedFoodProvider.getType() == FoodProviderType.CAFETERIA)
                .map(FetchedCafeteria.class::cast)
                .collect(Collectors.toList());
    }

    public void addFetchedCafeteria(FetchedFoodProvider fetchedCafeteria) {
        this.fetchedFoodProviders.add(fetchedCafeteria);
    }

    public boolean isEmpty() {
        return fetchedFoodProviders.isEmpty();
    }
}
