package com.triangl.trackingIngestion

import com.triangl.trackingIngestion.entity.Coordinate
import com.triangl.trackingIngestion.entity.Customer
import com.triangl.trackingIngestion.entity.Map
import com.triangl.trackingIngestion.entity.Router
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CustomerTest {

    private val router1 = Router("RouterId1", Coordinate(x = 1f, y = 2f))
    private val router2 = Router("RouterId2", Coordinate(x = 2f, y = 3f))
    private val router3 = Router("RouterId3", Coordinate(x = 3f, y = 4f))
    private val routerList = arrayListOf(router1, router2, router3)

    private val map = Map(id = "MapId1", name = "Map1", router = routerList)

    private val customer = Customer(name = "Customer1", maps = listOf(map))

    @Test
    fun `should parse Routers into Hashmap`() {
        /* When */
        val hashMapResult = customer.toRoutersHashmap()

        /* Then */
        MatcherAssert.assertThat(hashMapResult[router1.id], Matchers.`is`(router1))
        MatcherAssert.assertThat(hashMapResult[router2.id], Matchers.`is`(router2))
        MatcherAssert.assertThat(hashMapResult[router3.id], Matchers.`is`(router3))
    }
}