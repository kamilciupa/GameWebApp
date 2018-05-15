package com.gameweb.service;

import com.gameweb.DAO.CategoryDAO;
import com.gameweb.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryDAO categoryDAO;

    public List<Category> getCategories(){
        return categoryDAO.getCategories();
    }

}
