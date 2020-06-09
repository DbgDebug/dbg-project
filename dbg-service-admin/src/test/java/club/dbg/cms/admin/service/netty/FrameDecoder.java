package club.dbg.cms.admin.service.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class FrameDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        // 读取的开始下标
        int beginIndex = in.readerIndex();
        // 数据长度
        int length = in.readInt();
        // 当可读的数据小于数据长度时
        if (in.readableBytes() < length) {
            in.readerIndex(beginIndex);
            return;
        }
        // 设置读取下标，偏移下标到当前数据长度位置
        in.readerIndex(beginIndex + length);
        // 截取数据
        ByteBuf otherByteBufRef = in.slice(beginIndex, length);
        // 保持--
        otherByteBufRef.retain();
        out.add(otherByteBufRef);
    }
}
