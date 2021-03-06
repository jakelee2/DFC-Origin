<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!-- Messages Drawer -->
<!--
<div id="messages" class="tile drawer animated">
  <div class="listview narrow">
    <div class="media">
      <a href="javascript:;">Send a New Message</a> <span class="drawer-close">&times;</span>

    </div>
    <div class="overflow" style="height: 254px">
      <div class="media">
        <div class="pull-left">
          <img width="40" src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
        </div>
        <div class="media-body">
          <small class="text-muted">Super Admin - 2 Hours ago</small><br> <a class="t-overflow" href="javascript:;">Mauris
            consectetur urna nec tempor adipiscing. Proin sit amet nisi ligula. Sed eu adipiscing lectus</a>
        </div>
      </div>
      <div class="media">
        <div class="pull-left">
          <img width="40" src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
        </div>
        <div class="media-body">
          <small class="text-muted">Super Admin - 5 Hours ago</small><br> <a class="t-overflow" href="javascript:;">Suspendisse
            in purus ut nibh placerat Cras pulvinar euismod nunc quis gravida. Suspendisse pharetra</a>
        </div>
      </div>
      <div class="media">
        <div class="pull-left">
          <img width="40" src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
        </div>
        <div class="media-body">
          <small class="text-muted">Super Admin - On 15/12/2013</small><br> <a class="t-overflow"
            href="javascript:;">Maecenas venenatis enim condimentum ultrices fringilla. Nulla eget libero rhoncus,
            bibendum diam eleifend, vulputate mi. Fusce non nibh pulvinar, ornare turpis id</a>
        </div>
      </div>
      <div class="media">
        <div class="pull-left">
          <img width="40" src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
        </div>
        <div class="media-body">
          <small class="text-muted">Super Admin - On 14/12/2013</small><br> <a class="t-overflow"
            href="javascript:;">Phasellus interdum felis enim, eu bibendum ipsum tristique vitae. Phasellus feugiat
            massa orci, sed viverra felis aliquet quis. Curabitur vel blandit odio. Vestibulum sagittis quis sem sit
            amet tristique.</a>
        </div>
      </div>
      <div class="media">
        <div class="pull-left">
          <img width="40" src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
        </div>
        <div class="media-body">
          <small class="text-muted">Super Admin - On 15/12/2013</small><br> <a class="t-overflow"
            href="javascript:;">Ipsum wintoo consectetur urna nec tempor adipiscing. Proin sit amet nisi ligula. Sed
            eu adipiscing lectus</a>
        </div>
      </div>
      <div class="media">
        <div class="pull-left">
          <img width="40" src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
        </div>
        <div class="media-body">
          <small class="text-muted">Super Admin - On 16/12/2013</small><br> <a class="t-overflow"
            href="javascript:;">Suspendisse in purus ut nibh placerat Cras pulvinar euismod nunc quis gravida.
            Suspendisse pharetra</a>
        </div>
      </div>
      <div class="media">
        <div class="pull-left">
          <img width="40" src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
        </div>
        <div class="media-body">
          <small class="text-muted">Super Admin - On 17/12/2013</small><br> <a class="t-overflow"
            href="javascript:;">Maecenas venenatis enim condimentum ultrices fringilla. Nulla eget libero rhoncus,
            bibendum diam eleifend, vulputate mi. Fusce non nibh pulvinar, ornare turpis id</a>
        </div>
      </div>
      <div class="media">
        <div class="pull-left">
          <img width="40" src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
        </div>
        <div class="media-body">
          <small class="text-muted">Super Admin - On 18/12/2013</small><br> <a class="t-overflow"
            href="javascript:;">Phasellus interdum felis enim, eu bibendum ipsum tristique vitae. Phasellus feugiat
            massa orci, sed viverra felis aliquet quis. Curabitur vel blandit odio. Vestibulum sagittis quis sem sit
            amet tristique.</a>
        </div>
      </div>
      <div class="media">
        <div class="pull-left">
          <img width="40" src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
        </div>
        <div class="media-body">
          <small class="text-muted">Wendy Mitchell - On 19/12/2013</small><br> <a class="t-overflow"
            href="javascript:;">Integer a eros dapibus, vehicula quam accumsan, tincidunt purus</a>
        </div>
      </div>
    </div>
    <div class="media text-center whiter l-100">
      <a href="javascript:;"><small>VIEW ALL</small></a>
    </div>
  </div>
</div>

<div id="notifications" class="tile drawer animated">
  <div class="listview narrow">
    <div class="media">
      <a href="javascript:;">Notification Settings</a> <span class="drawer-close">&times;</span>
    </div>
    <div class="overflow" style="height: 254px">
      <div class="media">
        <div class="pull-left">
          <img width="40" src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
        </div>
        <div class="media-body">
          <small class="text-muted">Super Admin - 2 Hours ago</small><br> <a class="t-overflow" href="javascript:;">Mauris
            consectetur urna nec tempor adipiscing. Proin sit amet nisi ligula. Sed eu adipiscing lectus</a>
        </div>
      </div>
      <div class="media">
        <div class="pull-left">
          <img width="40" src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
        </div>
        <div class="media-body">
          <small class="text-muted">Super Admin - 5 Hours ago</small><br> <a class="t-overflow" href="javascript:;">Suspendisse
            in purus ut nibh placerat Cras pulvinar euismod nunc quis gravida. Suspendisse pharetra</a>
        </div>
      </div>
      <div class="media">
        <div class="pull-left">
          <img width="40" src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
        </div>
        <div class="media-body">
          <small class="text-muted">Super Admin - On 15/12/2013</small><br> <a class="t-overflow"
            href="javascript:;">Maecenas venenatis enim condimentum ultrices fringilla. Nulla eget libero rhoncus,
            bibendum diam eleifend, vulputate mi. Fusce non nibh pulvinar, ornare turpis id</a>
        </div>
      </div>
      <div class="media">
        <div class="pull-left">
          <img width="40" src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
        </div>
        <div class="media-body">
          <small class="text-muted">Mitch Bradberry - On 14/12/2013</small><br> <a class="t-overflow"
            href="javascript:;">Phasellus interdum felis enim, eu bibendum ipsum tristique vitae. Phasellus feugiat
            massa orci, sed viverra felis aliquet quis. Curabitur vel blandit odio. Vestibulum sagittis quis sem sit
            amet tristique.</a>
        </div>
      </div>
      <div class="media">
        <div class="pull-left">
          <img width="40" src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
        </div>
        <div class="media-body">
          <small class="text-muted">Nadin Jackson - On 15/12/2013</small><br> <a class="t-overflow"
            href="javascript:;">Ipsum wintoo consectetur urna nec tempor adipiscing. Proin sit amet nisi ligula. Sed
            eu adipiscing lectus</a>
        </div>
      </div>
      <div class="media">
        <div class="pull-left">
          <img width="40" src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
        </div>
        <div class="media-body">
          <small class="text-muted">David Villa - On 16/12/2013</small><br> <a class="t-overflow"
            href="javascript:;">Suspendisse in purus ut nibh placerat Cras pulvinar euismod nunc quis gravida.
            Suspendisse pharetra</a>
        </div>
      </div>
    </div>
    <div class="media text-center whiter l-100">
      <a href="javascript:;"><small>VIEW ALL</small></a>
    </div>
  </div>
</div>
-->