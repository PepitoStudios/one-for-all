package com.unatxe.quicklist.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.unatxe.quicklist.data.utils.DatabasePopulation
import com.unatxe.quicklist.domain.entities.QList
import com.unatxe.quicklist.domain.entities.QListItem
import com.unatxe.quicklist.domain.repository.QListRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class TestListInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var qListRepository: QListRepository

    @Inject
    lateinit var qListDatabase: QuickListDatabase

    @Before
    fun init() {
        hiltRule.inject()
        DatabasePopulation.populateDbTest(qListDatabase)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        qListDatabase.close()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.unatxe.quicklist.data.test", appContext.packageName)
    }

    @Test
    fun insertList() {
        runBlocking {
            val qList = QList(
                name = "Hello World",
                isFavourite = true,
                createdAt = DateTime(),
                updatedAt = DateTime()
            )
            qListRepository.insertList(qList).collect {
                assertEquals(it.name, "Hello World")
                assertEquals(it.isFavourite, true)
            }
        }
    }

    @Test
    fun getList() {
        runBlocking {
            qListRepository.getList(1).collect {
                assertEquals(it.name, "Lista 1")
                assertEquals(it.id, 1)
            }
        }
    }

    @Test
    fun getAllList() {
        try {
            runBlocking(Dispatchers.IO) {
                qListRepository.getLists().collect {
                    assertEquals(it.size, 5)
                    this@runBlocking.cancel()
                }
            }
        } catch (_: CancellationException) {}
    }

    @Test
    fun deleteList() {
        try {
            runBlocking {
                qListRepository.deleteList(
                    QList(1, "Lista 1", false, createdAt = DateTime(), updatedAt = DateTime())
                ).collect {
                    assertEquals(it, 1)
                }

                qListRepository.getLists().collect {
                    assertEquals(it.size, 4)
                    this@runBlocking.cancel()
                }
            }

            runBlocking(Dispatchers.IO) {
                qListRepository.getLists().collect {
                    assertEquals(it.size, 5)
                    this@runBlocking.cancel()
                }
            }
        } catch (_: CancellationException) {}
    }

    @Test
    fun updateList() {
        runBlocking {
            qListRepository.updateList(
                QList(
                    1,
                    "NYAM!",
                    isFavourite = true,
                    createdAt = DateTime(),
                    updatedAt = DateTime()
                )
            ).collect {
                assertEquals(it.id, 1)
                assertEquals(it.name, "NYAM!")
                assertEquals(it.isFavourite, true)
            }
        }
    }

    @Test
    fun getItemList() {
        runBlocking {
            qListRepository.getList(1).collect {
                assertEquals(3, it.items.size)
                assertEquals(true, it.items[1].checked)
                assertEquals("Item 2", it.items[1].text)
            }
        }
    }

    @Test
    fun insertItemList() {
        runBlocking {
            qListRepository.insertListItem(
                QListItem(
                    text = "Item 1",
                    checked = true,
                    qList = QList(
                        id = 1,
                        "Something",
                        false,
                        createdAt = DateTime(),
                        updatedAt = DateTime()
                    )
                )
            )
                .collect {
                    assertEquals("Item 1", it.text)
                    assertEquals(true, it.checked)
                }

            qListRepository.getList(1).collect {
                assertEquals(4, it.items.size)
            }
        }
    }

    @Test
    fun deleteItemList() {
        runBlocking {
            qListRepository.deleteListItem(
                QListItem(
                    1,
                    "Item 1",
                    false,
                    QList(
                        id = 1,
                        "Something",
                        false,
                        createdAt = DateTime(),
                        updatedAt = DateTime()
                    )
                )
            )
                .collect {
                    assertEquals(it, 1)
                }

            qListRepository.getList(1).collect {
                assertEquals(2, it.items.size)
            }
        }
    }

    @Test
    fun updateItemList() {
        runBlocking {
            qListRepository.updateListItem(
                QListItem(
                    1,
                    "Item Changed",
                    true,
                    QList(
                        id = 1,
                        "Something",
                        false,
                        createdAt = DateTime(),
                        updatedAt = DateTime()
                    )
                )
            ).collect {
                assertEquals("Item Changed", it.text)
                assertEquals(true, it.checked)
            }
        }
    }
}
