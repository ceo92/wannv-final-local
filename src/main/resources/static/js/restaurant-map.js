document.addEventListener("DOMContentLoaded", function () {
    // 지도 초기화
    var map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng(37.5665, 126.9780), // 초기 중심 좌표
        zoom: 10
    });

    // 마커 생성 함수
    function addMarkers(restaurants) {
        restaurants.forEach(function(restaurant) {
            var marker = new naver.maps.Marker({
                position: new naver.maps.LatLng(restaurant.latitude, restaurant.longitude),
                map: map
            });

            var infoWindow = new naver.maps.InfoWindow({
                content: `<div>${restaurant.name}</div>`
            });

            naver.maps.Event.addListener(marker, 'click', function() {
                location.href = `/restaurants/${restaurant.id}`;
            });

            naver.maps.Event.addListener(marker, 'mouseover', function() {
                infoWindow.open(map, marker);
            });

            naver.maps.Event.addListener(marker, 'mouseout', function() {
                infoWindow.close();
            });
        });
    }

    // HTML에서 전달된 데이터를 전역 변수로 읽어 사용
    if (window.restaurantData) {
        addMarkers(window.restaurantData);
    }
});