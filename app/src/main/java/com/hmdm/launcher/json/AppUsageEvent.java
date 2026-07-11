/*
 * Headwind MDM: Open Source Android MDM Software
 * https://h-mdm.com
 *
 * Copyright (C) 2019 Headwind Solutions LLC (http://h-sms.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hmdm.launcher.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A single "application brought to foreground" event, forming the lightweight
 * app-usage history reported to the server. The most recent event is the app the
 * user is currently (or was last) using. Collected only when the usage-access
 * permission has been granted.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppUsageEvent {

    @JsonIgnore
    private Long id;
    private String pkg;
    private String name;
    private Long ts;
    private Long startedAt;
    private Long endedAt;
    private Long durationMs;

    public AppUsageEvent() {
    }

    public AppUsageEvent(String pkg, String name, Long ts) {
        this.pkg = pkg;
        this.name = name;
        this.ts = ts;
    }

    public AppUsageEvent(String pkg, String name, Long startedAt, Long endedAt) {
        this.pkg = pkg;
        this.name = name;
        this.ts = startedAt;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        if (startedAt != null && endedAt != null && endedAt >= startedAt) {
            this.durationMs = endedAt - startedAt;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public Long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Long startedAt) {
        this.startedAt = startedAt;
    }

    public Long getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Long endedAt) {
        this.endedAt = endedAt;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }
}
