package com.bangkit.scade.data.source.local

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.bangkit.scade.data.source.local.entity.DataEntity
import com.bangkit.scade.data.source.local.room.DataDao

class LocalDataSource private constructor(private val dataDao: DataDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(dataDao: DataDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(dataDao)
    }

    fun getSessionToken(): LiveData<DataEntity> = dataDao.getSessionToken()


    fun insertDataCheck(data: DataEntity) {
        dataDao.insert(data)
    }

    fun deleteDataCheck(data: DataEntity) {
        dataDao.delete(data)
    }

    fun checkDataExist(id: Int): LiveData<Boolean> {
        val query = SimpleSQLiteQuery("SELECT EXISTS(SELECT * FROM session where id = $id)")
        return dataDao.checkDataExist(query)
    }
}