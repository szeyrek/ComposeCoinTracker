package com.scz.cointracker.repository

import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.domain.util.Resource
import com.scz.cointracker.network.CoinService
import com.scz.cointracker.network.model.CoinDtoMapper
import com.scz.cointracker.room.CoinDao
import com.scz.cointracker.room.CoinEntity

class CoinRepository_Impl(
    private val dao: CoinDao,
    private val coinService: CoinService,
    private val mapper: CoinDtoMapper
) : CoinRepository {

    override suspend fun getCoins(currency: String): Resource<List<Coin>> {
        return try {
            val response = coinService.getCoins(currency)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(mapper.mapFromDtoList(it))
                } ?: Resource.error("Null Data", null)
            } else {
                Resource.error("Error", null)
            }
        } catch (e: Exception) {
            Resource.error("Error", null)
        }
    }

    override suspend fun getCoins(): List<CoinEntity> {
        return dao.getCoins()
    }

    override suspend fun insertCoin(coin: CoinEntity) {
        dao.insertCoin(coin)
    }

    override suspend fun deleteCoin(id: Int) {
        dao.deleteCoin(id)
    }
}