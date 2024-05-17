package com.example.learnopensearch.service;

import com.example.learnopensearch.model.Widget;
import com.example.learnopensearch.model.WidgetRepository;

import java.util.List;
import java.util.Optional;

public interface WidgetService {

    List<Widget> findByName(String name);
    List<Widget> findByBrandName(String brandName);
    Optional<Widget> findBySerialNumber(String serialNumber);
    List<Widget> getAll();
    Optional<Widget> getById(String id);
    Widget create(Widget widget) throws DuplicateSerialNumberException;
    void deleteById(String id);
    Widget update(String id, Widget widget) throws WidgetNotFoundException;
    List<Widget> findByNameAndBrandName(String name, String brandName);

    List<Widget> search(String query);

}

