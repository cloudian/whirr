/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.whirr.service;

import java.io.IOException;

import org.apache.whirr.Cluster;

/**
 * Interface for cluster state storage facilities.
 * 
 */
public abstract class ClusterStateStore {

  /**
   * Deserializes cluster state from storage.
   * 
   * @return
   * @throws IOException
   */
  public abstract Cluster load() throws IOException;

  /**
   * Saves cluster state to storage.
   * 
   * @param cluster
   * @throws IOException
   */
  public abstract void save(Cluster cluster) throws IOException;

  /**
   * Destroys the provided cluster's state in storage.
   * 
   * @throws IOException
   */
  public abstract void destroy() throws IOException;

}
