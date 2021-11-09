/*
 * Copyright (C) 2010-2021 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.midpoint.ws;

import static com.evolveum.midpoint.schema.constants.SchemaConstants.NS_CHANNEL;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.namespace.QName;

import org.apache.commons.lang.Validate;
import org.apache.cxf.interceptor.Fault;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import com.evolveum.midpoint.model.api.ModelService;
import com.evolveum.midpoint.model.impl.security.SecurityHelper;
import com.evolveum.midpoint.prism.PrismConstants;
import com.evolveum.midpoint.prism.PrismContext;
import com.evolveum.midpoint.prism.PrismObject;
import com.evolveum.midpoint.prism.polystring.PolyString;
import com.evolveum.midpoint.prism.query.ObjectQuery;
import com.evolveum.midpoint.security.api.ConnectionEnvironment;
import com.evolveum.midpoint.security.api.SecurityUtil;
import com.evolveum.midpoint.task.api.Task;
import com.evolveum.midpoint.task.api.TaskManager;
import com.evolveum.midpoint.util.QNameUtil;
import com.evolveum.midpoint.util.exception.*;
import com.evolveum.midpoint.util.logging.Trace;
import com.evolveum.midpoint.util.logging.TraceManager;
import com.evolveum.midpoint.xml.ns._public.common.common_3.FocusType;
import com.evolveum.midpoint.xml.ns._public.common.common_3.UserType;

@SuppressWarnings("unused")
@WebService(targetNamespace = "https://midpoint.example.com/xml/ns/custom-soap-1")
public class ExampleSoapService {

    private static final Trace LOGGER = TraceManager.getTrace(ExampleSoapService.class);

    public static final QName CHANNEL_SOAP_EXAMPLE_QNAME =
            new QName(NS_CHANNEL, "soap-example");
    public static final String CHANNEL_SOAP_EXAMPLE_URI =
            QNameUtil.qNameToUri(CHANNEL_SOAP_EXAMPLE_QNAME);

    @Autowired private ModelService modelService;
    @Autowired private PrismContext prismContext;
    @Autowired private TaskManager taskManager;
    @Autowired private SecurityHelper securityHelper;

    // not part of the WS, as public method must be excluded explicitly
    @PostConstruct
    @WebMethod(exclude = true)
    public void init() {
        LOGGER.info("SOAP service initialized");
    }

    @WebMethod(operationName = "self")
    @WebResult(name = "user")
    public CustomUser self() {
        Task task = initRequest("self");
        try {
            FocusType loggedInUser = SecurityUtil.getPrincipal().getFocus();
            System.out.println("loggedInUser = " + loggedInUser);
            UserType user = modelService.getObject(
                            UserType.class, loggedInUser.getOid(), null, task, task.getResult())
                    .asObjectable();
            CustomUser customUser = createCustomUser(user);
            System.out.println("Going to return " + customUser);
            return customUser;
        } catch (ObjectNotFoundException | SchemaException | SecurityViolationException |
                ConfigurationException | CommunicationException | ExpressionEvaluationException e) {
            throw new Fault(e);
        } finally {
            finishRequest(task);
        }
    }

    @WebMethod(operationName = "searchUserByEmail")
    @WebResult(name = "users")
    public SearchUserByEmailResponse searchUserByEmail(SearchUserByEmailRequest parameters)
            throws Fault {
        Task task = initRequest("searchUserByEmail");

        Validate.notEmpty(parameters.email, "No email in person");

        try {

            ObjectQuery query = prismContext.queryFor(UserType.class)
                    .item((QName) UserType.F_EMAIL_ADDRESS)
                    .startsWith(parameters.email)
                    .matching(PrismConstants.STRING_IGNORE_CASE_MATCHING_RULE_NAME)
                    .build();
            List<PrismObject<UserType>> users = modelService.searchObjects(
                    UserType.class, query, null, task, task.getResult());
            // task result check omitted

            SearchUserByEmailResponse response = new SearchUserByEmailResponse();
            response.users = users.stream()
                    .map(p -> createCustomUser(p.asObjectable()))
                    .collect(Collectors.toList());
            return response;
        } catch (CommonException e) {
            throw new Fault(e);
        } finally {
            finishRequest(task);
        }
    }

    @NotNull
    private CustomUser createCustomUser(UserType user) {
        CustomUser customUser = new CustomUser();
        customUser.oid = user.getOid();
        // this should not be null
        customUser.username = user.getName().getOrig();
        // but this may, hence the static method
        customUser.fullname = PolyString.getOrig(user.getFullName());
        customUser.email = user.getEmailAddress();
        return customUser;
    }

    private Task initRequest(String operationName) {
        LOGGER.debug("INIT request: {}", operationName);

        // No need to audit login, it was already audited during authentication.
        Task task = taskManager.createTaskInstance(
                ExampleSoapService.class.getName() + "." + operationName);
        task.setChannel(CHANNEL_SOAP_EXAMPLE_URI);
        return task;
    }

    private void finishRequest(Task task) {
        task.getResult().computeStatus();
        ConnectionEnvironment connEnv = ConnectionEnvironment.create(CHANNEL_SOAP_EXAMPLE_URI);
        connEnv.setSessionIdOverride(task.getTaskIdentifier());
        securityHelper.auditLogout(connEnv, task, task.getResult());
    }
}