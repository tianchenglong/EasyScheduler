/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.escheduler.api.controller;


import cn.escheduler.api.enums.Status;
import cn.escheduler.api.service.TenantService;
import cn.escheduler.api.utils.Constants;
import cn.escheduler.api.utils.Result;
import cn.escheduler.dao.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static cn.escheduler.api.enums.Status.*;


/**
 * tenant controller
 */
@RestController
@RequestMapping("/tenant")
public class TenantController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(TenantController.class);


    @Autowired
    private TenantService tenantService;

    /**
     * create tenant
     *
     * @param loginUser
     * @param tenantCode
     * @param tenantName
     * @param queueId
     * @param desc
     * @return
     */
    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Result createTenant(@RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                                       @RequestParam(value = "tenantCode") String tenantCode,
                                                       @RequestParam(value = "tenantName") String tenantName,
                                                       @RequestParam(value = "queueId") int queueId,
                                                       @RequestParam(value = "desc",required = false) String desc) {
        logger.info("login user {}, create tenant, tenantCode: {}, tenantName: {}, queueId: {}, desc: {}",
                loginUser.getUserName(), tenantCode, tenantName, queueId,desc);
        try {
            Map<String, Object> result = tenantService.createTenant(loginUser,tenantCode,tenantName,queueId,desc);
            return returnDataList(result);

        }catch (Exception e){
            logger.error(CREATE_TENANT_ERROR.getMsg(),e);
            return error(CREATE_TENANT_ERROR.getCode(), CREATE_TENANT_ERROR.getMsg());
        }
    }


    /**
     * query tenant list paging
     *
     * @param loginUser
     * @param pageNo
     * @param searchVal
     * @param pageSize
     * @return
     */
    @GetMapping(value="/list-paging")
    @ResponseStatus(HttpStatus.OK)
    public Result queryTenantlistPaging(@RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                                     @RequestParam("pageNo") Integer pageNo,
                                                     @RequestParam(value = "searchVal", required = false) String searchVal,
                                                     @RequestParam("pageSize") Integer pageSize){
        logger.info("login user {}, list paging, pageNo: {}, searchVal: {}, pageSize: {}",
                loginUser.getUserName(),pageNo,searchVal,pageSize);
        try{
            Map<String, Object> result = checkPageParams(pageNo, pageSize);
            if(result.get(Constants.STATUS) != Status.SUCCESS){
                return returnDataListPaging(result);
            }
            result = tenantService.queryTenantList(loginUser, searchVal, pageNo, pageSize);
            return returnDataListPaging(result);
        }catch (Exception e){
            logger.error(QUERY_TENANT_LIST_PAGING_ERROR.getMsg(),e);
            return error(Status.QUERY_TENANT_LIST_PAGING_ERROR.getCode(), Status.QUERY_TENANT_LIST_PAGING_ERROR.getMsg());
        }
    }


    /**
     * tenant list
     *
     * @param loginUser
     * @return
     */
    @GetMapping(value="/list")
    @ResponseStatus(HttpStatus.OK)
    public Result queryTenantlist(@RequestAttribute(value = Constants.SESSION_USER) User loginUser){
        logger.info("login user {}, query tenant list");
        try{
            Map<String, Object> result = tenantService.queryTenantList(loginUser);
            return returnDataList(result);
        }catch (Exception e){
            logger.error(QUERY_TENANT_LIST_ERROR.getMsg(),e);
            return error(Status.QUERY_TENANT_LIST_ERROR.getCode(), Status.QUERY_TENANT_LIST_ERROR.getMsg());
        }
    }



    /**
     * udpate tenant
     *
     * @param loginUser
     * @param tenantCode
     * @param tenantName
     * @param queueId
     * @param desc
     * @return
     */
    @PostMapping(value = "/update")
    @ResponseStatus(HttpStatus.OK)
    public Result updateTenant(@RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                                       @RequestParam(value = "id") int id,
                                                       @RequestParam(value = "tenantCode") String tenantCode,
                                                       @RequestParam(value = "tenantName") String tenantName,
                                                       @RequestParam(value = "queueId") int queueId,
                                                       @RequestParam(value = "desc",required = false) String desc) {
        logger.info("login user {}, updateProcessInstance tenant, tenantCode: {}, tenantName: {}, queueId: {}, desc: {}",
                loginUser.getUserName(), tenantCode, tenantName, queueId,desc);
        try {
            Map<String, Object> result = tenantService.updateTenant(loginUser,id,tenantCode, tenantName, queueId, desc);
            return returnDataList(result);
        }catch (Exception e){
            logger.error(UPDATE_TENANT_ERROR.getMsg(),e);
            return error(Status.UPDATE_TENANT_ERROR.getCode(),UPDATE_TENANT_ERROR.getMsg());
        }
    }

    /**
     * delete tenant by id
     *
     * @param loginUser
     * @param id
     * @return
     */
    @PostMapping(value = "/delete")
    @ResponseStatus(HttpStatus.OK)
    public Result deleteTenantById(@RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                   @RequestParam(value = "id") int id) {
        logger.info("login user {}, delete tenant, tenantCode: {},", loginUser.getUserName(), id);
        try {
            Map<String, Object> result = tenantService.deleteTenantById(loginUser,id);
            return returnDataList(result);
        }catch (Exception e){
            logger.error(DELETE_TENANT_BY_ID_ERROR.getMsg(),e);
            return error(Status.DELETE_TENANT_BY_ID_ERROR.getCode(), Status.DELETE_TENANT_BY_ID_ERROR.getMsg());
        }
    }


    /**
     * verify tenant code
     *
     * @param loginUser
     * @param tenantCode
     * @return
     */
    @GetMapping(value = "/verify-tenant-code")
    @ResponseStatus(HttpStatus.OK)
    public Result verifyTenantCode(@RequestAttribute(value = Constants.SESSION_USER) User loginUser,
                                   @RequestParam(value ="tenantCode") String tenantCode
    ) {

        try{
            logger.info("login user {}, verfiy tenant code: {}",
                    loginUser.getUserName(),tenantCode);
            return tenantService.verifyTenantCode(tenantCode);
        }catch (Exception e){
            logger.error(VERIFY_TENANT_CODE_ERROR.getMsg(),e);
            return error(Status.VERIFY_TENANT_CODE_ERROR.getCode(), Status.VERIFY_TENANT_CODE_ERROR.getMsg());
        }
    }


}
