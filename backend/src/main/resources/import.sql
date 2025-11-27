INSERT INTO sucursales (id, nombre, slug, direccion, ciudad, branding_primary_color, branding_highlight_color, branding_accent_color, branding_headline, branding_tagline) VALUES
    ('11111111-1111-1111-1111-111111111111', 'Barbería Centro', 'centro', 'Cra 10 #20-30', 'Ciudad', '#0f172a', '#e11d48', '#10b981', 'Barbería Centro', 'Cortes impecables en el corazón de la ciudad'),
    ('22222222-2222-2222-2222-222222222222', 'Barbería Norte', 'norte', 'Av 5 #120-10', 'Ciudad', '#111827', '#8b5cf6', '#f59e0b', 'Barbería Norte', 'Experiencias premium y spa');

INSERT INTO availability_rules (id, day_of_week, open_time, close_time, online, sucursal_id) VALUES
    ('31111111-1111-1111-1111-111111111111', 'MONDAY', '09:00:00', '19:00:00', true, '11111111-1111-1111-1111-111111111111'),
    ('32222222-2222-2222-2222-222222222221', 'TUESDAY', '09:00:00', '19:00:00', true, '11111111-1111-1111-1111-111111111111'),
    ('33333333-3333-3333-3333-333333333333', 'WEDNESDAY', '09:00:00', '19:00:00', true, '11111111-1111-1111-1111-111111111111'),
    ('34444444-4444-4444-4444-444444444444', 'THURSDAY', '09:00:00', '19:00:00', true, '11111111-1111-1111-1111-111111111111'),
    ('35555555-5555-5555-5555-555555555555', 'FRIDAY', '09:00:00', '19:00:00', true, '11111111-1111-1111-1111-111111111111'),
    ('36666666-6666-6666-6666-666666666666', 'TUESDAY', '10:00:00', '21:00:00', true, '22222222-2222-2222-2222-222222222222'),
    ('37777777-7777-7777-7777-777777777777', 'WEDNESDAY', '10:00:00', '21:00:00', true, '22222222-2222-2222-2222-222222222222'),
    ('38888888-8888-8888-8888-888888888888', 'THURSDAY', '10:00:00', '21:00:00', true, '22222222-2222-2222-2222-222222222222'),
    ('39999999-9999-9999-9999-999999999999', 'FRIDAY', '10:00:00', '21:00:00', true, '22222222-2222-2222-2222-222222222222'),
    ('30000000-0000-0000-0000-000000000000', 'SATURDAY', '10:00:00', '21:00:00', true, '22222222-2222-2222-2222-222222222222');

INSERT INTO catalog_items (id, name, description, type, price, duration_minutes, sucursal_id) VALUES
    ('41111111-1111-1111-1111-111111111111', 'Corte clásico', 'Corte y estilo con toalla caliente', 'servicio', 35000, 40, '11111111-1111-1111-1111-111111111111'),
    ('42222222-2222-2222-2222-222222222222', 'Afeitado de lujo', 'Ritual de afeitado con aceites esenciales', 'servicio', 42000, 35, '11111111-1111-1111-1111-111111111111'),
    ('43333333-3333-3333-3333-333333333333', 'Kit de cuidado de barba', 'Incluye aceite y cepillo de barba', 'producto', 52000, null, '11111111-1111-1111-1111-111111111111'),
    ('44444444-4444-4444-4444-444444444444', 'Corte premium', 'Corte + diseño de barba con vapor ozono', 'servicio', 52000, 50, '22222222-2222-2222-2222-222222222222'),
    ('45555555-5555-5555-5555-555555555555', 'Color fantasía', 'Color personalizado con tratamiento', 'servicio', 78000, 75, '22222222-2222-2222-2222-222222222222'),
    ('46666666-6666-6666-6666-666666666666', 'Kit spa facial', 'Limpieza profunda, mascarilla y hidratación', 'producto', 63000, null, '22222222-2222-2222-2222-222222222222');

INSERT INTO barbers (id, name, services_csv, sucursal_id) VALUES
    ('51111111-1111-1111-1111-111111111111', 'Carlos', 'Corte clásico,Arreglo de barba', '11111111-1111-1111-1111-111111111111'),
    ('52222222-2222-2222-2222-222222222222', 'María', 'Fade,Color', '11111111-1111-1111-1111-111111111111'),
    ('53333333-3333-3333-3333-333333333333', 'Lucía', 'Color fantasía,Keratina', '22222222-2222-2222-2222-222222222222');
