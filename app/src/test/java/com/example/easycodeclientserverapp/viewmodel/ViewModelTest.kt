package com.example.easycodeclientserverapp.viewmodel

import com.example.easycodeclientserverapp.data.callback.ResultCallback
import com.example.easycodeclientserverapp.data.callback.JokeUiCallback
import com.example.easycodeclientserverapp.data.error.Error
import com.example.easycodeclientserverapp.data.repository.Repository
import com.example.easycodeclientserverapp.view.ViewModel
import org.junit.Assert.*
import org.junit.Test


class ViewModelTest {

    @Test
    fun test_success(){
        val model = FakeModel()
        model.returnSuccess = true
        val viewModel = ViewModel(model)
        viewModel.init(object : JokeUiCallback {
            override fun provideText(text: String) {
                assertEquals("joke one" + "\n" + "punchline", text)
            }

            override fun provideIconRes(id: Int) {
                TODO("Not yet implemented")
            }
        })

        viewModel.getJoke()
    }

    @Test
    fun test_error(){
        val model = FakeModel()
        model.returnSuccess = false
        val viewModel = ViewModel(model)
        viewModel.init(object : JokeUiCallback {
            override fun provideText(text: String) {
                assertEquals("fake error message", text)
            }

            override fun provideIconRes(id: Int) {
                TODO("Not yet implemented")
            }
        })

        viewModel.getJoke()
    }

}

class FakeModel : Repository {

    private var callback : ResultCallback?= null
    var returnSuccess = false

    override fun getJoke() {
//        if(returnSuccess){
//            callback?.provideJoke()
//            callback?.provideSuccess(Joke("joke one", "punchline"))
//        }else{
//            callback?.provideJoke(FakeError())
//        }
    }

    override fun clear() {
        callback = null
    }

    override fun init(callback: ResultCallback) {
        this.callback = callback
    }

}

class FakeError : Error {
    override fun message(): String {
        return "fake error message"
    }

}