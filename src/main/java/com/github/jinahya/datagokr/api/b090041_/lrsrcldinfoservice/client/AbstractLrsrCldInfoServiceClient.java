package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Response;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * An abstract parent class for client classes.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
@Slf4j
public abstract class AbstractLrsrCldInfoServiceClient {

    // -----------------------------------------------------------------------------------------------------------------
    public static final String BASE_URL = "http://apis.data.go.kr/B090041/openapi/service/LrsrCldInfoService";

    public static final String BASE_URL_DEVELOPMENT = BASE_URL;

    public static final String BASE_URL_PRODUCTION = BASE_URL_DEVELOPMENT;

    // ---------------------------------------------------------------------------------------------------- ?ServiceKey=
    protected static final String QUERY_PARAM_NAME_SERVICE_KEY = "ServiceKey";

    // ---------------------------------------------------------------------------------------------------------- ?sol*=
    protected static final String QUERY_PARAM_NAME_SOL_YEAR = "solYear";

    protected static final String QUERY_PARAM_NAME_SOL_MONTH = "solMonth";

    protected static final String QUERY_PARAM_NAME_SOL_DAY = "solDay";

    // ---------------------------------------------------------------------------------------------------------- ?lun*=
    protected static final String QUERY_PARAM_NAME_LUN_YEAR = "lunYear";

    protected static final String QUERY_PARAM_NAME_LUN_MONTH = "lunMonth";

    protected static final String QUERY_PARAM_NAME_LUN_DAY = "lunDay";

    // -----------------------------------------------------------------------------------------------------------------
    protected static final String QUERY_PARAM_NAME_PAGE_NO = "pageNo";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * An injection qualifier for the {@code ServiceKey} parameter.
     */
    @Qualifier
    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LrsrCldInfoServiceServiceKey {

    }

    // -----------------------------------------------------------------------------------------------------------------
    protected @NotNull List<Response.Body.Item> getItems(@Valid @NotNull final Response response) {
        requireNonNull(response, "response is null");
        {
            final Set<ConstraintViolation<Response>> violations = validator().validate(response);
            if (!violations.isEmpty()) {
                throw new RuntimeException("invalid response: " + response,
                                           new ConstraintViolationException(violations));
            }
        }
        if (!response.getHeader().isResultCodeSuccess()) {
            throw new RuntimeException("unsuccessful response: " + response);
        }
        return response.getBody().getItems();
    }

    // -----------------------------------------------------------------------------------------------------------------
    @LrsrCldInfoServiceServiceKey
    @Autowired
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(value = AccessLevel.PROTECTED)
    private String serviceKey;

    @Autowired
    @Accessors(fluent = true)
    @Setter(AccessLevel.NONE)
    @Getter(value = AccessLevel.PROTECTED)
    private Validator validator;
}
