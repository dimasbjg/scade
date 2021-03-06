package com.bangkit.scade.data.source.remote

import com.bangkit.scade.data.source.remote.request.InvoiceRequest
import com.bangkit.scade.data.source.remote.request.LoginRequest
import com.bangkit.scade.data.source.remote.request.RegisterRequest
import com.bangkit.scade.data.source.remote.request.UpdateHospitalRequest
import com.bangkit.scade.data.source.remote.response.*
import com.bangkit.scade.service.ApiBackendInterface
import com.bangkit.scade.service.ApiMLInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RemoteDataSource constructor(
    private val apiMLService: ApiMLInterface,
    private val apiBackendService: ApiBackendInterface
) {

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(
            apiMLService: ApiMLInterface,
            apiBackendService: ApiBackendInterface
        ): RemoteDataSource =
            instance ?: synchronized(this) {
                RemoteDataSource(apiMLService, apiBackendService).apply { instance = this }
            }
    }

    suspend fun uploadImage(image: MultipartBody.Part): SkinImageResponse {
        return apiMLService.uploadImage(image)
    }

    suspend fun getListEnglish(): ArticlesResponse {
        return apiBackendService.getArticleListEnglish()
    }

    suspend fun getListIndonesia(): ArticlesResponse {
        return apiBackendService.getArticleListIndonesia()
    }

    suspend fun getListInvoices(token: String): InvoicesListResponse {
        return apiBackendService.getListInvoices(token)
    }

    suspend fun getListHospital(): HospitalResponse {
        return apiBackendService.getListHospital()
    }

    suspend fun getSearchHospital(query: String): HospitalResponse {
        return apiBackendService.getSearchHospital(query)
    }

    suspend fun checkSession(token: String): SessionResponse {
        return apiBackendService.checkSession(token)
    }

    suspend fun getDetailDiagnoses(token: String, id: Int): DiagnosesByIdResponse {
        return apiBackendService.getDetailDiagnoses(token, id)
    }

    suspend fun getDetailHospital(id: Int): HospitalByIdResponse {
        return apiBackendService.getDetailHospital(id)
    }

    suspend fun getDetailInvoices(token: String, id: Int): InvoicesByIdResponse {
        return apiBackendService.getDetailInvoices(token, id)
    }

    suspend fun createInvoice(token: String, invoiceData: InvoiceRequest): InvoiceResponse {
        return apiBackendService.createInvoice(token, invoiceData)
    }

    suspend fun login(loginData: LoginRequest): LoginResponse {
        return apiBackendService.login(loginData)
    }

    suspend fun register(registerData: RegisterRequest): RegisterResponse {
        return apiBackendService.register(registerData)
    }

    suspend fun createDiagnoses(
        token: String,
        cancerName: RequestBody,
        image: MultipartBody.Part,
        position: RequestBody
    ): DiagnosesResponse {
        return apiBackendService.createDiagnoses(token, cancerName, image, position)
    }

    suspend fun updateHospitalInvoice(
        token: String,
        updateData: UpdateHospitalRequest,
        id: Int
    ): UpdateHospitalResponse {
        return apiBackendService.updateHispitalInvoice(token, updateData, id)
    }
}