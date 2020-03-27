INSERT INTO REQUEST_EVENTS (id, name, lat, lng, occured, recorded) VALUES (1, 'First event', '53', '10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO REQUEST_EVENTS (id, name, lat, lng, occured, recorded) VALUES (2, 'Second event', '51', '10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO REQUEST_EVENTS (id, name, lat, lng, occured, recorded) VALUES (3, 'Third event', '51', '09', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO APPROVE_REQUEST_EVENTS (id, event_request_id, occured, recorded) VALUES (1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO REJECT_REQUEST_EVENTS (id, event_request_id, occured, recorded) VALUES (1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);