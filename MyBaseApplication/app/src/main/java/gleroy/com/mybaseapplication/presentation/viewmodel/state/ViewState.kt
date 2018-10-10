package gleroy.com.mybaseapplication.presentation.viewmodel.state

sealed class ViewState {

    class MainState : ViewState()

    class InitState : ViewState()

    class EmptyState : ViewState()

    class ErrorState(val error: Error) : ViewState()
}


