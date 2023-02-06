let startDate = '2023-01-01',endDate = '2023-01-02';
let url = 'https://www.sfcservice.com/billing/billing/order-fee-detail?code=&bill_type=0&write_off=&balance_start_date=' + startDate + '&balance_end_date=' + endDate + '&act=send&searchType=excel';
let xhr = new XMLHttpRequest();
xhr.open('get',url,false);
xhr.send();
xhr.responseText;


