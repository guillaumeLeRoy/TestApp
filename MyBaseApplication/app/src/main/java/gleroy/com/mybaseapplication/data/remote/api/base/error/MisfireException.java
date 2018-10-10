package gleroy.com.mybaseapplication.data.remote.api.base.error;

/**
 * Raise this exception when the request didn't start.
 */
public class MisfireException extends AJobException {

    public MisfireException() {
        super(null, null, null);
    }

}
