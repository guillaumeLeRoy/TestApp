package gleroy.com.mybaseapplication.data.repository

import gleroy.com.mybaseapplication.data.local.MyLocalSource
import gleroy.com.mybaseapplication.data.local.entity.MyLocalObject
import gleroy.com.mybaseapplication.data.remote.source.MyRemoteSource
import gleroy.com.mybaseapplication.data.repository.base.IRepository
import gleroy.com.mybaseapplication.data.request.RequestParameter
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyRepository @Inject constructor(private val remoteSource: MyRemoteSource,
                                       private val localSource: MyLocalSource) : IRepository<MyLocalObject> {

    override fun fetch(param: RequestParameter): Maybe<MyLocalObject> {
        val response = remoteSource.get(param).map { MyLocalObject() }
        return response
    }

}