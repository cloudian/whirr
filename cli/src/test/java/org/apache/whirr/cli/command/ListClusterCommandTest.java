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

package org.apache.whirr.cli.command;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Set;

import org.apache.whirr.service.ClusterSpec;
import org.apache.whirr.service.Service;
import org.apache.whirr.service.ServiceFactory;
import org.hamcrest.Matcher;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeState;
import org.jclouds.compute.domain.internal.NodeMetadataImpl;
import org.jclouds.domain.LocationScope;
import org.jclouds.domain.internal.LocationImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.StringContains;

public class ListClusterCommandTest {

  private ByteArrayOutputStream outBytes;
  private PrintStream out;
  private ByteArrayOutputStream errBytes;
  private PrintStream err;

  @Before
  public void setUp() {
    outBytes = new ByteArrayOutputStream();
    out = new PrintStream(outBytes);
    errBytes = new ByteArrayOutputStream();
    err = new PrintStream(errBytes);
  }
  
  @Test
  public void testInsufficientOptions() throws Exception {
    ListClusterCommand command = new ListClusterCommand();
    int rc = command.run(null, null, err, Collections.<String>emptyList());
    assertThat(rc, is(-1));
    assertThat(errBytes.toString(), containsUsageString());
  }
  
  private Matcher<String> containsUsageString() {
    return StringContains.containsString("Usage: whirr list-cluster [OPTIONS]");
  }
  
  @Test
  public void testAllOptions() throws Exception {
    
    ServiceFactory factory = mock(ServiceFactory.class);
    Service service = mock(Service.class);
    when(factory.create((String) any())).thenReturn(service);
    NodeMetadata node1 = new NodeMetadataImpl(null, "name1", "id1",
        new LocationImpl(LocationScope.PROVIDER, "location-id1",
            "location-desc1", null),
        null, Collections.<String,String>emptyMap(), null, null, "image-id",
        null, NodeState.RUNNING,
        Lists.newArrayList("100.0.0.1"),
        Lists.newArrayList("10.0.0.1"), null);
    NodeMetadata node2 = new NodeMetadataImpl(null, "name2", "id2",
        new LocationImpl(LocationScope.PROVIDER, "location-id2",
            "location-desc2", null),
        null, Collections.<String,String>emptyMap(), null, null, "image-id",
        null, NodeState.RUNNING,
        Lists.newArrayList("100.0.0.2"),
        Lists.newArrayList("10.0.0.2"), null);
    when(service.getNodes((ClusterSpec) any())).thenReturn(
        (Set) Sets.newLinkedHashSet(Lists.newArrayList(node1, node2)));
    
    ListClusterCommand command = new ListClusterCommand(factory);
    
    int rc = command.run(null, out, null, Lists.newArrayList(
        "--service-name", "test-service",
        "--cluster-name", "test-cluster",
        "--identity", "myusername"));
    
    assertThat(rc, is(0));
    
    assertThat(outBytes.toString(), is(
      "id1\timage-id\t100.0.0.1\t10.0.0.1\tRUNNING\tlocation-id1\n" +
      "id2\timage-id\t100.0.0.2\t10.0.0.2\tRUNNING\tlocation-id2\n"));
    
    verify(factory).create("test-service");
    
  }
}
