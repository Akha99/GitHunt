package com.example.githunt.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githunt.data.local.FavoriteUser
import com.example.githunt.data.local.FavoriteUserDao
import com.example.githunt.data.local.UserDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {


    private val userDao: FavoriteUserDao by lazy {
        UserDatabase.getDatabase(application).favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? = userDao.getFavoriteUser()

}