package learn.htm.projectlearn.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import learn.htm.projectlearn.BR
import learn.htm.projectlearn.R
import learn.htm.projectlearn.extension.showDialog
import learn.htm.projectlearn.utils.autoCleared

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {
    abstract val viewModel: V
    var viewDataBinding by autoCleared<T>()
    private var errorMessageDialog: AlertDialog? = null

    @get:LayoutRes
    abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.apply {
            setVariable(BR.viewModel, viewModel)
            executePendingBindings()
            lifecycleOwner = this@BaseFragment
        }
        observeEvent()
    }

    protected open fun observeEvent() {
        viewModel.apply {
            /*httpUnauthorized.observe(viewLifecycleOwner, Observer {

            })*/
            unexpectedError.observe(viewLifecycleOwner, Observer {
                handleErrorMessage(message = getString(R.string.unexpected_error))
            })
            httpUnavailableError.observe(viewLifecycleOwner, Observer {
                handleErrorMessage(message = getString(R.string.http_unavailable_error))
            })
            rxMapperError.observe(viewLifecycleOwner, Observer {
                handleErrorMessage(message = getString(R.string.rx_mapper_error))
            })
            httpForbiddenError.observe(viewLifecycleOwner, Observer {
                handleErrorMessage(message = getString(R.string.http_forbidden_error))
            })
            httpGatewayTimeoutError.observe(viewLifecycleOwner, Observer {
                handleErrorMessage(message = getString(R.string.no_internet_error))
            })
            errorMessage.observe(viewLifecycleOwner, Observer {
                handleErrorMessage(message = it)
            })
        }
    }

    private fun handleErrorMessage(message: String) {
        if (errorMessageDialog?.isShowing != true) {
            errorMessageDialog =
                context?.showDialog(message = message, positiveMessage = getString(R.string.ok))
        }
    }
}