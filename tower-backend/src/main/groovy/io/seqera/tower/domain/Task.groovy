/*
 * Copyright (c) 2019, Seqera Labs.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * This Source Code Form is "Incompatible With Secondary Licenses", as
 * defined by the Mozilla Public License, v. 2.0.
 */

package io.seqera.tower.domain


import java.time.OffsetDateTime

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.databind.ObjectMapper
import grails.gorm.annotation.Entity
import groovy.transform.CompileDynamic
import groovy.transform.ToString
import io.seqera.tower.enums.TaskStatus
/**
 * Workflow task info
 * see https://www.nextflow.io/docs/latest/tracing.html#execution-report
 */
@ToString(includeNames = true)
@Entity
@JsonIgnoreProperties(['dirtyPropertyNames', 'errors', 'dirty', 'attached', 'workflow', 'workflowId'])
@CompileDynamic
class Task {

    static final private ObjectMapper mapper = new ObjectMapper().findAndRegisterModules()

    static belongsTo = [workflow: Workflow]

    /**
     * The order of the task in the workflow
     */
    Long taskId
    String hash
    String name
    String process
    String tag

    TaskStatus status

    OffsetDateTime submit
    OffsetDateTime start
    OffsetDateTime complete

    /**
     * Multi-value field encoded as JSON
     */
    String module
    String container
    Integer attempt
    String script
    String scratch
    String workdir

    String queue
    Integer cpus
    Long memory
    Long disk
    String time
    String env

    String errorAction

    Long exitStatus
    Long duration
    Long realtime
    Long nativeId

    Double pcpu
    Double pmem
    Long rss
    Long vmem
    Long peakRss
    Long peakVmem
    Long rchar
    Long wchar
    Long syscr
    Long syscw
    Long readBytes
    Long writeBytes

    Long volCtxt
    Long invCtxt

    boolean checkIsSubmitted() {
        status == TaskStatus.SUBMITTED
    }

    boolean checkIsRunning() {
        status == TaskStatus.RUNNING
    }

    boolean checkIsSucceeded() {
        (status == TaskStatus.COMPLETED) && !errorAction
    }

    boolean checkIsFailed() {
        status == TaskStatus.FAILED
    }

    @JsonSetter('module')
    void deserializeModuleJson(List<String> moduleList) {
        module = moduleList ? mapper.writeValueAsString(moduleList) : null
    }

    @JsonSetter('exit')
    void deserializeExistStatus(Long exit) {
        exitStatus = exit
    }

    @JsonGetter('exit')
    String serializeExitStatus() {
        exitStatus
    }

    static constraints = {
        taskId(unique: 'workflow')

        hash(maxSize: 12)
        process(nullable: true, maxSize: 255)
        tag(nullable: true, maxSize: 255)
        name(maxSize: 255)
        exitStatus(nullable: true)
        submit(nullable: true)
        start(nullable: true)
        complete(nullable: true)
        module(nullable: true, maxSize: 255)
        container(nullable: true, maxSize: 255)
        attempt(nullable: true)
        script(nullable: true)
        scratch(nullable: true)
        workdir(nullable: true, maxSize: 512)
        queue(nullable: true, maxSize: 100)
        cpus(nullable: true)
        memory(nullable: true)
        disk(nullable: true)
        time(nullable: true)
        env(nullable: true, maxSize: 2048)
        errorAction(nullable: true)
        duration(nullable: true)
        realtime(nullable: true)
        nativeId(nullable: true)
        pcpu(nullable: true)
        pmem(nullable: true)
        rss(nullable: true)
        vmem(nullable: true)
        peakRss(nullable: true)
        peakVmem(nullable: true)
        rchar(nullable: true)
        wchar(nullable: true)
        syscr(nullable: true)
        syscw(nullable: true)
        readBytes(nullable: true)
        writeBytes(nullable: true)
        volCtxt(nullable: true)
        invCtxt(nullable: true)
    }

    static mapping = {
        script(type: 'text')
    }
}
